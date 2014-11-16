package fr.univ_artois.iut_lens.spaceinvader;

import java.util.ArrayList;

import fr.univ_artois.iut_lens.spaceinvader.entities.*;

/**
 * 
 * Classe � part car le ship a ses propres contr�les
 * Elle n'h�rites pas d'autre classe
 *
 */
public class ShipManager {
	private EntitiesManager entitiesManager;
	
	private Entity ship;
	private double moveSpeed = 300; // Vitesse de d�placement du vaisseau
	private long lastFire = 0; //Dernier tir du vaisseau
	private long fireInterval = 200; //Intervalle de temps par défaut pour lequel le vaisseau peut tirer
	private ShootManager shootManager  = new ShootManager(); //Gestionnaire de tire du vaisseau
	private ArrayList<String> shipType = new ArrayList<String>(); //Type of ship (ref)
	private int actualShipType = 0;  //Type de vaisseau actuel
	
	
	public ShipManager(EntitiesManager eM) {
		init();
		this.entitiesManager = eM;
	}
	
	/**
	 * initialise les différents vaisseaux
	 */
	public void init() {
		shipType.add("defaultship.png");
		shipType.add("spaceship1.png");
		shipType.add("supership1.png");
	}
	
	public void moveShip(int i) {
		
		//Si la commande est -1 on va � gauche
		if(i == -1)
			ship.setHorizontalMovement(-moveSpeed);
		
		//Si la commande est 1  on va � droite
		if(i == 1)
			ship.setHorizontalMovement(moveSpeed);
		
		if(i == 0) //Si la commande est 0 on stoppe le vaisseau
			ship.setHorizontalMovement(0);
	}
	
	//Retourner le vaisseau actuel
	public Entity newShip() {
		ship = new EntityShip("sprites/"+ shipType.get(actualShipType), 370, 540, entitiesManager); //Cr�ation d'un vaisseau et insertion dans la gestion des entit�s (collision etc...)
		return ship;
	}
	
	//Voir si le vaisseau peut tirer
	public void tryToShoot(Long actualTime, EntitiesManager eM, Game g) {
		if(actualTime - lastFire < fireInterval) return; //L'interval de tir est trop court
		
		lastFire = actualTime; //On met le dernier tire au temps actuel
		
		if(actualShipType==0) { //Si on est dans le premier vaisseau
			if(fireInterval>200) fireInterval=200; //L'intervale de tire est réduit au minumum à 200 
			eM.getEntitiesList().add(shootManager.getShoot(g, eM, ship.getX()+27-5, ship.getY(), 0));
		}
		if(actualShipType==1) { //Si on est dans le premier vaisseau
			if(fireInterval>100) fireInterval=100; //L'intervale de tire est réduit au minumum à 100 
			eM.getEntitiesList().add(shootManager.getShoot(g, eM, ship.getX()+20-5, ship.getY(), 0));
			eM.getEntitiesList().add(shootManager.getShoot(g, eM, ship.getX()+20+5, ship.getY(), 0));
		}
		if(actualShipType==2) { //Si on est dans le premier vaisseau
			if(fireInterval>50) fireInterval=50; //L'intervale de tire est réduit au minumum à 50 
			eM.getEntitiesList().add(shootManager.getShoot(g, eM, ship.getX()+30-12, ship.getY(), 0));
			eM.getEntitiesList().add(shootManager.getShoot(g, eM, ship.getX()+30-6, ship.getY(), 0));
			eM.getEntitiesList().add(shootManager.getShoot(g, eM, ship.getX()+30, ship.getY(), 0));
			eM.getEntitiesList().add(shootManager.getShoot(g, eM, ship.getX()+30+6, ship.getY(), 0));
			eM.getEntitiesList().add(shootManager.getShoot(g, eM, ship.getX()+2012, ship.getY(), 0));
		}
	}
	
	public void increaseShipType() {
		if(actualShipType<shipType.size()-1) actualShipType++;
	}
}
