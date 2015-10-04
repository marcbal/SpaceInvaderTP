package fr.univ_artois.iut_lens.spaceinvader.server.players;

import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClient;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientCommand;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientCommand.Direction;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientNextLevel;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientTogglePause;
import fr.univ_artois.iut_lens.spaceinvader.server.Server;
import fr.univ_artois.iut_lens.spaceinvader.server.network.ServerConnection;

public class Player {
	
	private PlayerConnection connection;
	
	private ShipManager shipManager = new ShipManager(Server.serverInstance.entitiesManager, this);
	
	// ship command
	private Direction shipDirection = Direction.NONE;
	private boolean shipSooting = false;
	
	// global command
	private boolean askedForNextLevel = false;
	
	private AtomicBoolean dead = new AtomicBoolean(false);
	
	public final String name;
	
	public Player(String n, SocketAddress addr, ServerConnection co) {
		connection = new PlayerConnection(addr, co);
		name = n;
	}
	
	
	
	public PlayerConnection getConnection() { return connection; }
	public ShipManager getShipManager() { return shipManager; }
	
	
	public synchronized void setShipDirection(Direction dir) { shipDirection = dir; }
	public synchronized Direction getShipDirection() { return shipDirection; }
	
	public synchronized void setShipShooting(boolean s) { shipSooting = s; }
	public synchronized boolean isShipShooting() { return shipSooting; }
	
	public synchronized void askForNextLevel() { askedForNextLevel = true; }
	public synchronized boolean hasAskedForNextLevel() { return askedForNextLevel; }
	public synchronized void reinitAskingForNextLevel() { askedForNextLevel = false; }
	
	
	public void handlePacket(PacketClient packet) {
		if (packet instanceof PacketClientCommand) {
			setShipDirection(((PacketClientCommand)packet).getDirection());
			setShipShooting(((PacketClientCommand)packet).isShooting());
		}
		else if (packet instanceof PacketClientNextLevel) {
			askForNextLevel();
		}
		else if (packet instanceof PacketClientTogglePause) {
			Server.serverInstance.setPause(((PacketClientTogglePause)packet).getPause());
		}
		else {
			throw new RuntimeException("The packet '"+packet.getClass().getCanonicalName()+"' is not handled by Player.hendlePacket()");
		}
	}
	
	
	public void applyCommandToGame() {
		shipManager.moveShip(getShipDirection());
		if (isShipShooting())
			shipManager.tryToShoot(Server.getCurrentGameNanoTime()/1000000/* convert to milliseconds */);
	}



	public boolean isDead() {
		return dead.get();
	}



	public void die() {
		dead.set(true);
		shipManager.decreaseShipType();
	}
	
	public void initNewLevel() {
		dead.set(false);
	}
	
	
	
	
}
