package fr.univ_artois.iut_lens.spaceinvader;

import java.util.ArrayList;
import java.util.List;

import fr.univ_artois.iut_lens.spaceinvader.entities.ship.*;
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
	
	private int actualShip = 40;  //Type de vaisseau actuel
	
	private int newShip = actualShip;
	
	public ShipManager(EntitiesManager eM) {
		this.entitiesManager = eM;
		
		ships.add(new EntityShipDefault(entitiesManager));
		ships.add(new EntityShipPowered(entitiesManager));
		ships.add(new EntityShipPowered(entitiesManager));
		ships.add(new EntityShipPowered(entitiesManager));
		ships.add(new EntityShipPowered(entitiesManager));
		ships.add(new EntityShipSupership(entitiesManager));
		ships.add(new EntityShipSupership(entitiesManager));
		ships.add(new EntityShipSupership(entitiesManager));
		ships.add(new EntityShipSupership(entitiesManager));
		ships.add(new EntityShipSupership(entitiesManager));
		ships.add(new EntityShipMegaShip(entitiesManager));
		ships.add(new EntityShipMegaShip(entitiesManager));
		ships.add(new EntityShipMegaShip(entitiesManager));
		ships.add(new EntityShipMegaShip(entitiesManager));
		ships.add(new EntityShipMegaShip(entitiesManager));
		ships.add(new EntityShipMegaShip(entitiesManager));
		ships.add(new EntityShipMegaShip(entitiesManager));
		ships.add(new EntityShipMegaShip(entitiesManager));
		ships.add(new EntityShipMegaShip(entitiesManager));
		ships.add(new EntityShipMegaShip(entitiesManager));
		ships.add(new EntityShipConqueror(entitiesManager, 200, 1));
		ships.add(new EntityShipConqueror(entitiesManager, 125, 1));
		ships.add(new EntityShipConqueror(entitiesManager, 75, 1));
		ships.add(new EntityShipConqueror(entitiesManager, 150, 2));
		ships.add(new EntityShipConqueror(entitiesManager, 100, 2));
		ships.add(new EntityShipSecretShip(entitiesManager, 2500));
		ships.add(new EntityShipSecretShip(entitiesManager, 2300));
		ships.add(new EntityShipSecretShip(entitiesManager, 2100));
		ships.add(new EntityShipSecretShip(entitiesManager, 1700));
		ships.add(new EntityShipSecretShip(entitiesManager, 1600));
		ships.add(new EntityShipSecretShip(entitiesManager, 1500));
		ships.add(new EntityShipSecretShip(entitiesManager, 1400));
		ships.add(new EntityShipSecretShip(entitiesManager, 1300));
		ships.add(new EntityShipSecretShip(entitiesManager, 1200));
		ships.add(new EntityShipSecretShip(entitiesManager, 1000));
		ships.add(new EntityShipSecretShip(entitiesManager, 900));
		ships.add(new EntityShipSecretShip(entitiesManager, 850));
		ships.add(new EntityShipSecretShip(entitiesManager, 800));
		ships.add(new EntityShipSecretShip(entitiesManager, 750));
		ships.add(new EntityShipSecretShip(entitiesManager, 725));
		ships.add(new EntityShipSecretShip(entitiesManager, 700));
		ships.add(new EntityShipSecretShip(entitiesManager, 680));
		ships.add(new EntityShipSecretShip(entitiesManager, 660));
		ships.add(new EntityShipSecretShip(entitiesManager, 640));
		ships.add(new EntityShipSecretShip(entitiesManager, 620));
		ships.add(new EntityShipSecretShip(entitiesManager, 600));
		ships.add(new EntityShipConqueror(entitiesManager, 10, 10));
		ships.add(new EntityShipFinal2(entitiesManager, 1000, 500, 30, 2));
		ships.add(new EntityShipFinal2(entitiesManager, 800, 750, 15, 2));
		ships.add(new EntityShipFinal2(entitiesManager, 500, 1200, 1, 1));
		ships.add(new EntityShipFinal(entitiesManager, 1000));
	}
	
	public EntityShip getCurrentShip() {
		ships.get(actualShip).dontRemove();
		return ships.get(actualShip);
	}
	
	public void moveShip(int command) {
		//command vaut -1 (gauche), 0 (stop) ou 1 (droite)
		getCurrentShip().setHorizontalDirection(command);
	}
	
	//Tenter de tirer avec le vaisseau
	public void tryToShoot(long currentTime) {
		getCurrentShip().tryToShoot(currentTime);
	}
	
	//Faire évoluer le vaisseau
	public void increaseShipType() {
		//On vérifie qu'il ne soit pas déjà égal au max
		if(newShip<ships.size()-1) newShip++;
	}
	
	//Faire régrésser le vaisseau
	public void decreaseShipType() {
		//On verifie qu'il ne soit pas déjà égale  à 0
		if(newShip>0) newShip--;
	}
	
	public int getActualShip() {
		return actualShip;
	}
	
	public int getNewShip() {
		return newShip;
	}
	
	public void makeItEvolve() {
		if(newShip != actualShip) {
			Vector2d pos = ships.get(actualShip).getPosition();
			entitiesManager.removeEntity(getCurrentShip());
			ships.get(newShip).getPosition().x = pos.x+ships.get(actualShip).getBoundingBox().width/2D-ships.get(newShip).getBoundingBox().width/2D;
			ships.get(newShip).getPosition().y = pos.y;
			entitiesManager.getEntitiesList().add(ships.get(newShip));
			ships.get(newShip).setPosition(pos);
			actualShip = newShip;
		}
	}

	public int[] getShipProgress()
	{
		int[] r = new int[2];
		r[0] = actualShip+1;
		r[1] = ships.size();
		return r;
	}
	
	
	
	public void reinitAllShip() {
		for(EntityShip ship : ships) {
			ship.reinitShot();
		}
	}
}

