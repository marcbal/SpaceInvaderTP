package fr.univ_artois.iut_lens.spaceinvader;

import java.util.ArrayList;
import java.util.List;

import fr.univ_artois.iut_lens.spaceinvader.entities.ship.*;
import fr.univ_artois.iut_lens.spaceinvader.entities.shot.*;
import fr.univ_artois.iut_lens.spaceinvader.util.Position;

/**
 * 
 * Classe � part car le ship a ses propres contr�les
 * Elle n'h�rites pas d'autre classe
 *
 */
public class ShipManager {
	private EntitiesManager entitiesManager;
	
	private List<EntityShip> ships = new ArrayList<EntityShip>();
	private double moveSpeed = 300; // Vitesse de d�placement du vaisseau
	private long lastFireTime = 0; //Dernier tir du vaisseau
	private long fireInterval = 200; //Intervalle de temps par défaut pour lequel le vaisseau peut tirer
	private int actualShip = 0;  //Type de vaisseau actuel
	
	
	public ShipManager(EntitiesManager eM) {
		this.entitiesManager = eM;
		init();
	}
	
	/**
	 * initialise les différents vaisseaux
	 */
	public void init() {
		ships.add(new EntityShipDefault(entitiesManager));
		ships.add(new EntityShipPowered(entitiesManager));
		ships.add(new EntityShipSupership(entitiesManager));
	}
	
	public void moveShip(int i) {
		EntityShip ship = getCurrentShip();
		//Si la commande est -1 on va � gauche
		if(i == -1)
			ship.setHorizontalMovement(-moveSpeed);
		
		//Si la commande est 1  on va � droite
		if(i == 1)
			ship.setHorizontalMovement(moveSpeed);
		
		if(i == 0) //Si la commande est 0 on stoppe le vaisseau
			ship.setHorizontalMovement(0);
	}
	
	public EntityShip getCurrentShip() {
		return ships.get(actualShip);
	}
	
	//Voir si le vaisseau peut tirer
	public void tryToShoot(Long actualTime, EntitiesManager eM, Game g) {
		if(actualTime - lastFireTime < fireInterval) return; //L'interval de tir est trop court
		lastFireTime = actualTime; //On met le dernier tire au temps actuel

		EntityShip ship = getCurrentShip();
		
		if(actualShip==0) { //Si on est dans le premier vaisseau
			if(fireInterval>200) fireInterval=200; //L'intervale de tire est réduit au minumum à 200 
			eM.getEntitiesList().add(new EntityShotFromAllyBasic(ship.getX()+ship.getBoundingBox().width/2-5, ship.getY(), new Position(0, -500), eM));
		}
		if(actualShip==1) { //Si on est dans le premier vaisseau
			if(fireInterval>100) fireInterval=100; //L'intervale de tire est réduit au minumum à 100 
			eM.getEntitiesList().add(new EntityShotFromAllyBasic(ship.getX()+ship.getBoundingBox().width/2-10, ship.getY(), new Position(-100, -1000), eM));
			eM.getEntitiesList().add(new EntityShotFromAllyBasic(ship.getX()+ship.getBoundingBox().width/2, ship.getY(), new Position(100, -1000), eM));
		}
		if(actualShip==2) { //Si on est dans le premier vaisseau
			if(fireInterval>50) fireInterval=50; //L'intervale de tire est réduit au minumum à 50 
			eM.getEntitiesList().add(new EntityShotFromAllyAdvanced(ship.getX()+ship.getBoundingBox().width/2-20, ship.getY(), new Position(0, -300), eM));
			eM.getEntitiesList().add(new EntityShotFromAllyBasic(ship.getX()+ship.getBoundingBox().width/2-10, ship.getY(), new Position(-150, -1000), eM));
			eM.getEntitiesList().add(new EntityShotFromAllyBasic(ship.getX()+ship.getBoundingBox().width/2, ship.getY(), new Position(150, -1000), eM));
			eM.getEntitiesList().add(new EntityShotFromAllyAdvanced(ship.getX()+ship.getBoundingBox().width/2+10, ship.getY(), new Position(0, -300), eM));
		}
	}
	
	public void increaseShipType() {
		if(actualShip<ships.size()-1) actualShip++;
	}
}
