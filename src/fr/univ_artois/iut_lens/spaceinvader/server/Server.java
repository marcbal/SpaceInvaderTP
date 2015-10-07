package fr.univ_artois.iut_lens.spaceinvader.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerLevelStart;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateInfos;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerLevelEnd;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerLevelEnd.PlayerScore;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateInfos.GameInfo;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap.MapData;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap.MapData.EntityDataSpawn;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ship.EntityShip;
import fr.univ_artois.iut_lens.spaceinvader.server.network.ServerConnection;
import fr.univ_artois.iut_lens.spaceinvader.server.players.Player;
import fr.univ_artois.iut_lens.spaceinvader.server.players.PlayerManager;
import fr.univ_artois.iut_lens.spaceinvader.sprites_manager.SpriteStore;
import fr.univ_artois.iut_lens.spaceinvader.util.Logger;

public class Server extends Thread {
	
	public static Server serverInstance = null;
	
	public static void initServer(int port) throws IOException {
		if (serverInstance == null) {
			serverInstance = new Server(port);
			serverInstance.start();
		}
	}
	
	
	
	private static long currentGameNanoTime = 10000000000L;
	
	public static long getCurrentGameNanoTime() { return currentGameNanoTime; }
	/** True if the game is currently "running", i.e. the game loop is looping */
	public final AtomicBoolean gameRunning = new AtomicBoolean(true);
	
	
	public final EntitiesManager entitiesManager = new EntitiesManager();
	
	public final LevelManager levelManager = new LevelManager(entitiesManager);
	
	public final ServerConnection serverConnection;
	
	public final PlayerManager playerManager;
	
	
	public final AtomicBoolean commandPause = new AtomicBoolean(false);

	public final AtomicBoolean waitingForKeyPress = new AtomicBoolean(true);

	private BonusManager bonusManager = new BonusManager(entitiesManager);
	
	private AtomicReference<List<PlayerScore>> levelEndScore = new AtomicReference<List<PlayerScore>>(null);
	
	
	
	private Server(int port) throws IOException {
		super("Server");
		Logger.info("Initialisation du serveur Méga Space Invader sur le port "+port+" ...");
		
		serverConnection = new ServerConnection(port);
		
		playerManager = new PlayerManager();
		
		serverConnection.setListener(playerManager);
	}
	
	
	
	@Override
	public void run() {
		

		Logger.info("Serveur démarré avec succès");

		long delta = (long) (1000000000/MegaSpaceInvader.SERVER_TICK_PER_SECOND);
		// keep looping round til the game ends
		while (gameRunning.get()) {
			long loop_start = System.nanoTime();
			
			
			if (!commandPause.get())
				currentGameNanoTime += delta;
			
			handleEvent();
			
			updateLogic(delta);
			
			sendPackets();
			
			if (!waitingForKeyPress.get() && entitiesManager.getTotalRemainingEnnemyLife() <= 0)
				finishLevel(true);
			
			if (!waitingForKeyPress.get() && playerManager.everyPlayerDead())
				finishLevel(false);
			
            long loop_duration = System.nanoTime()-loop_start;
			try { Thread.sleep(Math.max(5, (delta-loop_duration)/1000000)); } catch (Exception e) {}
		}
		
		Logger.info("Arrêt du serveur ...");
		serverConnection.close();
	}
	
	
	
	
	private void handleEvent()
	{
		// resolve the movement of the ship. First assume the ship 
		// isn't moving. If either cursor key is pressed then
		// update the movement appropraitely
		
		if (waitingForKeyPress.get())
		{
			if (playerManager.isPlayerAskedForNextLevel()) {
				waitingForKeyPress.set(false);
				startLevel();
			}
				
		}
		
		
		playerManager.applyCommandToGame();
	}
	
	

	private void updateLogic(long delta) {
		if (!waitingForKeyPress.get() && !commandPause.get())
		{
			//Déplacer les entités
			//Vérifier si il y a eu des collisions
			//Supprimer les entités tués
			entitiesManager.moveAndCollideEntities(delta,levelManager);
			
			//Faire tirer les entités
			entitiesManager.makeEntitiesShoot(levelManager);
			
			
			//Générer des bonus
			bonusManager.performBonus();
		}
		
	}
	
	
	
	
	private void sendPackets() {
		// envoi des infos technique aux joueurs
		/*
		 * Envoi des infos de jeux aux joueurs
		 */
		if (!waitingForKeyPress.get()) {
			GameInfo globalGameInfos = new GameInfo();
			globalGameInfos.currentEnemyLife = entitiesManager.getTotalRemainingEnnemyLife();
			int[] levelProgress = levelManager.getLevelProgress();
			globalGameInfos.currentLevel = levelProgress[0];
			globalGameInfos.nbLevel = levelProgress[1];
			globalGameInfos.maxEnemyLife = levelManager.getCurrentLevel().getMaxEnemyLife();
			globalGameInfos.nbCollisionThreads = MegaSpaceInvader.SERVER_NB_THREAD_FOR_ENTITY_COLLISION;
			globalGameInfos.nbEntity = entitiesManager.getEntitiesList().size();
			PacketServerUpdateInfos packetInfos = new PacketServerUpdateInfos();
			packetInfos.setInfos(globalGameInfos);
			playerManager.sendToAll(packetInfos);
		}
		
		if (!waitingForKeyPress.get() && !commandPause.get()) {
			// TODO faire une méthode moins lourde pour le réseau
			
			/*
			 *  envoi des données de jeux (début de level, données complètes)
			 */
			MapData mapData = new MapData();
			
			mapData.spritesData = SpriteStore.get().getSpritesId(false);
			
			for (Entity e : Server.serverInstance.entitiesManager.getEntityListSnapshot()) {
				EntityDataSpawn eData = new EntityDataSpawn();
				eData.id = e.id;
				eData.currentLife = (e.getMaxLife() > 1) ? e.getLife() : 0;
				eData.maxLife = (e.getMaxLife() > 1) ? e.getMaxLife() : 0;
				eData.name = (e instanceof EntityShip) ? ((EntityShip)e).associatedShipManager.getPlayer().name : "";
				eData.spriteId = e.getSprite().id;
				eData.posX = e.getPosition().x;
				eData.posY = e.getPosition().y;
				eData.speedX = e.getSpeed().x;
				eData.speedY = e.getSpeed().y;
				mapData.spawningEntities.add(eData);
			}
			
			PacketServerUpdateMap packetMap = new PacketServerUpdateMap();
			packetMap.setEntityData(mapData);
			playerManager.sendToAll(packetMap);
			
			
			
		}
	}
	

	/**
	 * Notification that an alien has been killed
	 */
	public synchronized void notifyAlienKilled() {
		
		for(Entity entity : entitiesManager.getEntitiesList()) {
		    entity.setNotifyAlienKilled();
		}
	}
	
	
	/**
	 * 
	 */
	public void finishLevel(boolean win) {
		entitiesManager.getEntitiesList().clear(); //nettoie l'écran des entités
		waitingForKeyPress.set(true);
		if (win) {
			levelManager.goToNextLevel();
		} else {
			levelManager.goToFirstLevel();
		}
		
		// génération des scores de la partie
		List<PlayerScore> scores = new ArrayList<PlayerScore>();
		for (Player p : playerManager.getPlayers()) {
			PlayerScore sc = new PlayerScore();
			sc.playerName = p.name;
			sc.score = 0; // TODO définir le score
			scores.add(sc);
		}
		setLevelEndScore(scores);
		
		PacketServerLevelEnd packet = new PacketServerLevelEnd();
		packet.setScores(scores);
		playerManager.sendToAll(packet);
		
	}
	
	
	/**
	 * Start a fresh game, this should clear out any old data and
	 * create a new set.
	 */
	private void startLevel() {
		entitiesManager.getEntitiesList().clear();

		Logger.info("Starting new level !");
		
		setLevelEndScore(null);
		
		playerManager.reinitPlayersForNextLevel();
		
		playerManager.sendToAll(new PacketServerLevelStart());
		
		/*
		 *  envoi des données de jeux (début de level, données complètes)
		 */
		MapData mapData = new MapData();
		
		mapData.spritesData = SpriteStore.get().getSpritesId(false);
		
		for (Entity e : Server.serverInstance.entitiesManager.getEntityListSnapshot()) {
			EntityDataSpawn eData = new EntityDataSpawn();
			eData.id = e.id;
			eData.currentLife = (e.getMaxLife() > 1) ? e.getLife() : 0;
			eData.maxLife = (e.getMaxLife() > 1) ? e.getMaxLife() : 0;
			eData.name = (e instanceof EntityShip) ? ((EntityShip)e).associatedShipManager.getPlayer().name : "";
			eData.spriteId = e.getSprite().id;
			eData.posX = e.getPosition().x;
			eData.posY = e.getPosition().y;
			eData.speedX = e.getSpeed().x;
			eData.speedY = e.getSpeed().y;
			mapData.spawningEntities.add(eData);
		}
		
		PacketServerUpdateMap packetMap = new PacketServerUpdateMap();
		packetMap.setEntityData(mapData);
		playerManager.sendToAll(packetMap);
		
		
		// Placer les vaisseaux préalablement créés dans le tableau des entités
		entitiesManager.getEntitiesList().addAll(playerManager.reinitAllPlayersShips());
		
		// create block of aliens, with the arguments
		entitiesManager.getEntitiesList().addAll(levelManager.getCurrentLevel().getNewlyGeneratedLevel());
	}
	
	
	
	public void setPause(boolean p) {
		commandPause.set(p);
	}
	
	
	
	public static List<InetAddress> getAllNetworkInterfacesAddress() {
		List<InetAddress> addr = new ArrayList<InetAddress>();
		Enumeration<NetworkInterface> interfaces;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			return addr;
		}
		
		for (NetworkInterface interfce : Collections.list(interfaces)) {
	        for (InetAddress inetAddress : Collections.list(interfce.getInetAddresses())) {
	            addr.add(inetAddress);
	        }
		}
		
		return addr;
	}
	
	
	
	private void setLevelEndScore(List<PlayerScore> scores) {
		levelEndScore.set(scores);
	}
	
	public List<PlayerScore> getPlayerScores() {
		return levelEndScore.get();
	}
	
	
}
