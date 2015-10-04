package fr.univ_artois.iut_lens.spaceinvader.server.players;

import java.util.ArrayList;
import java.util.List;

import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientCommand;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ship.*;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

/**
 * 
 * Classe à part car le ship a ses propres contrôles
 * Elle n'hérites pas d'autre classe
 *
 */
public class ShipManager {
	private EntitiesManager entitiesManager;
	
	private List<EntityShip> ships = new ArrayList<EntityShip>();
	
	private int actualShipIndex = 40;  //Type de vaisseau actuel
	
	private int newShipIndex = actualShipIndex;
	
	private final Player player;
	
	public ShipManager(EntitiesManager eM, Player p) {
		entitiesManager = eM;
		player = p;
		
		
		ships.add(new EntityShipDefault(entitiesManager, this));
		ships.add(new EntityShipPowered(entitiesManager, this));
		ships.add(new EntityShipPowered(entitiesManager, this));
		ships.add(new EntityShipPowered(entitiesManager, this));
		ships.add(new EntityShipPowered(entitiesManager, this));
		ships.add(new EntityShipSupership(entitiesManager, this));
		ships.add(new EntityShipSupership(entitiesManager, this));
		ships.add(new EntityShipSupership(entitiesManager, this));
		ships.add(new EntityShipSupership(entitiesManager, this));
		ships.add(new EntityShipSupership(entitiesManager, this));
		ships.add(new EntityShipMegaShip(entitiesManager, this));
		ships.add(new EntityShipMegaShip(entitiesManager, this));
		ships.add(new EntityShipMegaShip(entitiesManager, this));
		ships.add(new EntityShipMegaShip(entitiesManager, this));
		ships.add(new EntityShipMegaShip(entitiesManager, this));
		ships.add(new EntityShipMegaShip(entitiesManager, this));
		ships.add(new EntityShipMegaShip(entitiesManager, this));
		ships.add(new EntityShipMegaShip(entitiesManager, this));
		ships.add(new EntityShipMegaShip(entitiesManager, this));
		ships.add(new EntityShipMegaShip(entitiesManager, this));
		ships.add(new EntityShipConqueror(entitiesManager, this, 200, 1));
		ships.add(new EntityShipConqueror(entitiesManager, this, 125, 1));
		ships.add(new EntityShipConqueror(entitiesManager, this, 75, 1));
		ships.add(new EntityShipConqueror(entitiesManager, this, 150, 2));
		ships.add(new EntityShipConqueror(entitiesManager, this, 100, 2));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 2500));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 2300));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 2100));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 1700));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 1600));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 1500));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 1400));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 1300));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 1200));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 1000));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 900));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 850));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 800));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 750));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 725));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 700));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 680));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 660));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 640));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 620));
		ships.add(new EntityShipSecretShip(entitiesManager, this, 600));
		ships.add(new EntityShipConqueror(entitiesManager, this, 10, 10));
		ships.add(new EntityShipFinal2(entitiesManager, this, 1000, 500, 30, 2));
		ships.add(new EntityShipFinal2(entitiesManager, this, 800, 750, 15, 2));
		ships.add(new EntityShipFinal2(entitiesManager, this, 500, 1200, 1, 1));
		ships.add(new EntityShipFinal(entitiesManager, this, 1000));
	}
	
	public EntityShip getCurrentShip() {
		ships.get(actualShipIndex).dontRemove();
		return ships.get(actualShipIndex);
	}
	
	public void moveShip(PacketClientCommand.Direction direction) {
		//command vaut -1 (gauche), 0 (stop) ou 1 (droite)
		getCurrentShip().setHorizontalDirection(direction);
	}
	
	//Tenter de tirer avec le vaisseau
	public void tryToShoot(long currentTime) {
		getCurrentShip().tryToShoot(currentTime);
	}
	
	//Faire évoluer le vaisseau
	public void increaseShipType() {
		//On vérifie qu'il ne soit pas déjà égal au max
		if(newShipIndex<ships.size()-1) newShipIndex++;
		processShipChange();
	}
	
	//Faire régrésser le vaisseau
	public void decreaseShipType() {
		//On verifie qu'il ne soit pas déjà égale  à 0
		if(newShipIndex>0) newShipIndex--;
		processShipChange();
	}
	
	public int getCurrentShipIndex() {
		return actualShipIndex;
	}
	
	public int getNewShipIndex() {
		return newShipIndex;
	}
	
	private void processShipChange() {
		if(newShipIndex != actualShipIndex) {
			Vector2d pos = ships.get(actualShipIndex).getPosition();
			entitiesManager.removeEntity(getCurrentShip());
			ships.get(newShipIndex).getPosition().x = pos.x+ships.get(actualShipIndex).getBoundingBox().width/2D-ships.get(newShipIndex).getBoundingBox().width/2D;
			ships.get(newShipIndex).getPosition().y = pos.y;
			entitiesManager.getEntitiesList().add(ships.get(newShipIndex));
			ships.get(newShipIndex).setPosition(pos);
			actualShipIndex = newShipIndex;
		}
	}

	public int[] getShipProgress()
	{
		int[] r = new int[2];
		r[0] = actualShipIndex+1;
		r[1] = ships.size();
		return r;
	}
	
	
	
	public void reinitAllShip() {
		for(EntityShip ship : ships) {
			ship.reinitShot();
		}
	}
	
	public Player getPlayer() {
		return player;
	}
}

