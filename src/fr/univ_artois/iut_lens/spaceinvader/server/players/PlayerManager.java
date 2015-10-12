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
import fr.univ_artois.iut_lens.spaceinvader.server.Server;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ship.EntityShip;
import fr.univ_artois.iut_lens.spaceinvader.server.network.NetworkReceiveListener;
import fr.univ_artois.iut_lens.spaceinvader.server.network.ServerConnection.InputConnectionThread;
import fr.univ_artois.iut_lens.spaceinvader.server.network.ServerConnection.InvalidClientMessage;
import fr.univ_artois.iut_lens.spaceinvader.util.Logger;

public class PlayerManager implements NetworkReceiveListener {

	private Map<InputConnectionThread, Player> playersByConnection = new HashMap<InputConnectionThread, Player>();
	private Map<String, Player> playersByName = new HashMap<String, Player>();
	
	public PlayerManager() {
	}
	
	public synchronized void hasDisconnected(InputConnectionThread co) {
		playersByConnection.remove(co);
	}
	
	public synchronized Player getPlayerByConnection(InputConnectionThread co) {
		return playersByConnection.get(co);
	}
	
	public synchronized Player getNewlyConnectedPlayer(InputConnectionThread co, String name) {
		Player p;
		
		if (playersByName.containsKey(name)) {
			p = playersByName.get(name);
			if (playersByConnection.containsValue(p))
				throw new InvalidClientMessage("Un joueur avec ce pseudo est déjà en ligne");
			p.setConnection(co);
		}
		else {
			p = new Player(name, co);
			playersByName.put(name, p);
		}
		playersByConnection.put(co, p);
		
		return p;
	}
	
	public synchronized void applyCommandToGame() {
		for (Player p : playersByConnection.values()) {
			p.applyCommandToGame();
		}
	}
	
	public synchronized List<EntityShip> reinitAllPlayersShips() {
		List<EntityShip> ships = new ArrayList<EntityShip>();
		for (Player p : playersByConnection.values()) {
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
		for (Player p : playersByConnection.values()) {
			if (p.hasAskedForNextLevel()) {
				ret = true;
				p.reinitAskingForNextLevel();
			}
		}
		return ret;
	}
	
	
	public synchronized void sendToAll(PacketServer packet) {
		for (Player p : playersByConnection.values()) {
			p.getConnection().send(packet);
		}
	}
	
	
	
	
	
	
	@Override
	public void onReceivePacket(InputConnectionThread co, PacketClient packet) {
		
		Player p = getPlayerByConnection(co);
		
		if (packet instanceof PacketClientJoin) {
			
			if (p != null)
				throw new InvalidClientMessage("Votre adresse réseau correspond déjà à un joueur en ligne");
			p = getNewlyConnectedPlayer(co, ((PacketClientJoin)packet).getPlayerName());
			p.getConnection().send(new PacketServerConnectionOk());
			Logger.info("Join : "+p.name+". "+getPlayersCount()+" player(s) online now.");
			
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
				p.getConnection().send(Server.serverInstance.createFullUpdateMapPacket());
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
			hasDisconnected(co);
			p.getConnection().send(new PacketServerDisconnectOk());
			p.getConnection().close();
			Logger.info("Disconnect : "+p.name+". "+getPlayersCount()+" player(s) left.");
		}
		else {
			p.handlePacket(packet);
		}
		
	}

	public synchronized Player[] getPlayersSnapshot() {
		return playersByConnection.values().toArray(new Player[playersByConnection.size()]);
	}

	public synchronized int getPlayersCount() {
		return playersByConnection.size();
	}

	public synchronized boolean everyPlayerDead() {
		for (Player p : playersByConnection.values()) {
			if (!p.isDead())
				return false;
		}
		return true;
	}
	
	
	public synchronized void reinitPlayersForNextLevel() {
		for (Player p : playersByConnection.values()) {
			p.initNewLevel();
		}
	}
	
	
	

}
