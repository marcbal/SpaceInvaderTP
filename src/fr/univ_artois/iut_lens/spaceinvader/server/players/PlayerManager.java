package fr.univ_artois.iut_lens.spaceinvader.server.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClient;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientDisconnect;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientJoin;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServer;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerConnectionOk;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerDisconnectOk;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerLevelEnd;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerLevelEnd.PlayerScore;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerLevelStart;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerTogglePause;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap.MapData;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap.MapData.EntityDataSpawn;
import fr.univ_artois.iut_lens.spaceinvader.server.Server;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ship.EntityShip;
import fr.univ_artois.iut_lens.spaceinvader.server.network.NetworkReceiveListener;
import fr.univ_artois.iut_lens.spaceinvader.server.network.ServerConnection.ConnectionThread;
import fr.univ_artois.iut_lens.spaceinvader.server.network.ServerConnection.InvalidClientMessage;
import fr.univ_artois.iut_lens.spaceinvader.sprites_manager.SpriteStore;

public class PlayerManager implements NetworkReceiveListener {
	
	private Map<ConnectionThread, Player> players = new HashMap<ConnectionThread, Player>();
	
	public PlayerManager() {
	}
	
	public synchronized void removePlayer(ConnectionThread co) {
		players.remove(co);
	}
	
	public synchronized Player getPlayerByConnection(ConnectionThread co) {
		return players.get(co);
	}
	
	public synchronized Player createPlayer(ConnectionThread co, String name) {
		Player p = new Player(name, co);
		
		players.put(co, p);
		
		return p;
	}
	
	public synchronized void applyCommandToGame() {
		for (Player p : players.values()) {
			p.applyCommandToGame();
		}
	}
	
	public synchronized List<EntityShip> reinitAllPlayersShips() {
		List<EntityShip> ships = new ArrayList<EntityShip>();
		for (Player p : players.values()) {
			p.getShipManager().reinitAllShip();
			
			EntityShip ship = p.getShipManager().getCurrentShip();
			ship.getPosition().x = MegaSpaceInvader.DISPLAY_WIDTH/2D - ship.getBoundingBox().width/2D;
			ship.getPosition().y = MegaSpaceInvader.DISPLAY_HEIGHT-50;
			
			ships.add(ship);
		}
		return ships;
	}
	
	
	public synchronized boolean isPlayerAskedForNextLevel() {
		boolean ret = false;
		for (Player p : players.values()) {
			if (p.hasAskedForNextLevel()) {
				ret = true;
				p.reinitAskingForNextLevel();
			}
		}
		return ret;
	}
	
	
	public synchronized void sendToAll(PacketServer packet) {
		for (Player p : players.values()) {
			p.getConnection().send(packet);
		}
	}
	
	
	
	
	
	
	@Override
	public void onReceivePacket(ConnectionThread co, PacketClient packet) {
		
		Player p = getPlayerByConnection(co);
		
		if (packet instanceof PacketClientJoin) {
			
			if (p != null)
				throw new InvalidClientMessage("Votre adresse réseau correspond déjà à un joueur en ligne");
			p = createPlayer(co, ((PacketClientJoin)packet).getPlayerName());
			p.getConnection().send(new PacketServerConnectionOk());
			
			// si la partie en cours se trouve entre deux niveaux (waitingForKeyPress)
			if (Server.serverInstance.waitingForKeyPress.get()) {
				List<PlayerScore> scores = Server.serverInstance.getPlayerScores();
				if (scores != null) {
					PacketServerLevelEnd packet1 = new PacketServerLevelEnd();
					packet1.setScores(scores);
					p.getConnection().send(packet1);
				}
			}
			else {
				// une partie est en cours
				p.die();
				p.getConnection().send(new PacketServerLevelStart());
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
				p.getConnection().send(packetMap);
			}
			if (Server.serverInstance.commandPause.get()) {
				PacketServerTogglePause packetPause = new PacketServerTogglePause();
				packetPause.setPause(true);
				p.getConnection().send(packetPause);
			}
			
			
			
			
			
		}
		else if (packet instanceof PacketClientDisconnect) {
			if (p == null)
				throw new InvalidClientMessage("Vous n'êtes pas connecté");
			removePlayer(co);
			p.getConnection().send(new PacketServerDisconnectOk());
			p.getConnection().close();
			synchronized (this) {
				if(players.size() == 0)
					Server.serverInstance.commandPause.set(true);
			}
		}
		else {
			p.handlePacket(packet);
		}
		
	}

	public synchronized Player[] getPlayers() {
		return players.values().toArray(new Player[players.size()]);
	}

	public synchronized boolean everyPlayerDead() {
		for (Player p : players.values()) {
			if (!p.isDead())
				return false;
		}
		return true;
	}
	
	
	public synchronized void reinitPlayersForNextLevel() {
		for (Player p : players.values()) {
			p.initNewLevel();
		}
	}
	

}
