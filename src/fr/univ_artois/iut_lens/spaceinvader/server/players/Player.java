package fr.univ_artois.iut_lens.spaceinvader.server.players;

import java.util.concurrent.atomic.AtomicBoolean;

import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClient;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientCommand;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientCommand.Direction;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerTogglePause;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientNextLevel;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientTogglePause;
import fr.univ_artois.iut_lens.spaceinvader.server.Server;
import fr.univ_artois.iut_lens.spaceinvader.server.network.ServerConnection.InputConnectionThread;
import fr.univ_artois.iut_lens.spaceinvader.util.Logger;

public class Player {
	
	private InputConnectionThread connection;
	
	private ShipManager shipManager = new ShipManager(Server.serverInstance.entitiesManager, this);
	
	// ship command
	private Direction shipDirection = Direction.NONE;
	private boolean shipSooting = false;
	
	// global command
	private boolean askedForNextLevel = false;
	
	private AtomicBoolean dead = new AtomicBoolean(false);
	
	public final String name;
	
	public Player(String n, InputConnectionThread co) {
		connection = co;
		name = n;
	}
	
	
	
	public InputConnectionThread getConnection() { return connection; }
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
			boolean pause = ((PacketClientTogglePause)packet).getPause();
			Server.serverInstance.setPause(pause);
			
			Logger.info(name+" set pause to "+pause);
			
			PacketServerTogglePause responsePacket = new PacketServerTogglePause();
			responsePacket.setPause(pause);
			Server.serverInstance.playerManager.sendToAll(responsePacket);
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
		setShipShooting(false);
		setShipDirection(Direction.NONE);
	}
	
	
	
	
}
