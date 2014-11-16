package fr.univ_artois.iut_lens.spaceinvader;

import java.util.ArrayList;
import java.util.List;

import fr.univ_artois.iut_lens.spaceinvader.entities.ship.*;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

/**
 * 
 * Classe � part car le ship a ses propres contr�les
 * Elle n'h�rites pas d'autre classe
 *
 */
public class ShipManager {
	private EntitiesManager entitiesManager;
	
	private List<EntityShip> ships = new ArrayList<EntityShip>();
	
	private int actualShip = 0;  //Type de vaisseau actuel
	
	private int newShip = actualShip;
	
	public ShipManager(EntitiesManager eM) {
		this.entitiesManager = eM;
		
		ships.add(new EntityShipDefault(entitiesManager));
		ships.add(new EntityShipPowered(entitiesManager));
		ships.add(new EntityShipPowered(entitiesManager));
		ships.add(new EntityShipSupership(entitiesManager));
		ships.add(new EntityShipSupership(entitiesManager));
		ships.add(new EntityShipSupership(entitiesManager));
		ships.add(new EntityShipMegaShip(entitiesManager));
	}
	
	public EntityShip getCurrentShip() {
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
	
	//Faire �voluer le vaisseau
	public void increaseShipType() {
		//On v�rifie qu'il ne soit pas d�j� �gal au max
		if(newShip<ships.size()-1) newShip++;
	}
	
	//Faire r�gr�sser le vaisseau
	public void decreaseShipType() {
		//On verifie qu'il ne soit pas d�j� �gale  � 0
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
			ships.get(newShip).getPosition().x = pos.x;
			ships.get(newShip).getPosition().y = pos.y;
			entitiesManager.getEntitiesList().add(ships.get(newShip));
			actualShip = newShip;
		}
	}
}

