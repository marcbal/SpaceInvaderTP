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
	
	private Map<InputConnectionThread, Player> players = new HashMap<InputConnectionThread, Player>();
	
	public PlayerManager() {
	}
	
	public synchronized void removePlayer(InputConnectionThread co) {
		players.remove(co);
	}
	
	public synchronized Player getPlayerByConnection(InputConnectionThread co) {
		return players.get(co);
	}
	
	public synchronized Player createPlayer(InputConnectionThread co, String name) {
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
	public void onReceivePacket(InputConnectionThread co, PacketClient packet) {
		
		Player p = getPlayerByConnection(co);
		
		if (packet instanceof PacketClientJoin) {
			
			if (p != null)
				throw new InvalidClientMessage("Votre adresse réseau correspond déjà à un joueur en ligne");
			p = createPlayer(co, ((PacketClientJoin)packet).getPlayerName());
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
			removePlayer(co);
			p.getConnection().send(new PacketServerDisconnectOk());
			p.getConnection().close();
			Logger.info("Disconnect : "+p.name+". "+getPlayersCount()+" player(s) left.");
			synchronized (this) {
				if(players.size() == 0) {
					Server.serverInstance.commandPause.set(true);
					Logger.info("Pause autoset to true (no player online)");
				}
			}
		}
		else {
			p.handlePacket(packet);
		}
		
	}

	public synchronized Player[] getPlayersSnapshot() {
		return players.values().toArray(new Player[players.size()]);
	}

	public synchronized int getPlayersCount() {
		return players.size();
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
