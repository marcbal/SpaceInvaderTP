package fr.univ_artois.iut_lens.spaceinvader.server;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.network.ServerConnection;
import fr.univ_artois.iut_lens.spaceinvader.server.players.PlayerManager;
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
	
	
	private final AtomicBoolean commandPause = new AtomicBoolean(false);

	private final AtomicBoolean waitingForKeyPress = new AtomicBoolean(true);

	private BonusManager bonusManager = new BonusManager(entitiesManager);
	
	
	
	
	
	private Server(int port) throws IOException {
		super("Server");
		Logger.info("Initialisation du serveur Méga Space Invader sur le port "+port+" ...");
		
		serverConnection = new ServerConnection(port);
		
		playerManager = new PlayerManager(serverConnection);
		
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
			
			// TODO vérifier si on a battu tout les énemies
			// si oui : finishLevel(true);
			
			if (!waitingForKeyPress.get()) {
				// TODO envoyer les mises à jours aux joueurs
			}
			
            long loop_duration = System.nanoTime()-loop_start;
			try { Thread.sleep(Math.max(5, (delta-loop_duration)/1000000)); } catch (Exception e) {}
		}
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
		
	}
	
	
	/**
	 * Start a fresh game, this should clear out any old data and
	 * create a new set.
	 */
	private void startLevel() {
		entitiesManager.getEntitiesList().clear();
		
		
		
		// Placer les vaisseaux préalablement créés dans le tableau des entités
		entitiesManager.getEntitiesList().addAll(playerManager.reinitAllPlayersShips());
		
		// create block of aliens, with the arguments
		entitiesManager.getEntitiesList().addAll(levelManager.getCurrentLevel().generateLevel());
	}
	
	
	
	public void setPause(boolean p) {
		commandPause.set(p);
	}
	
	
	
}
