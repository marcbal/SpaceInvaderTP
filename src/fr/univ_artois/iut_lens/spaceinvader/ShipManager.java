package fr.univ_artois.iut_lens.spaceinvader;

import fr.univ_artois.iut_lens.spaceinvader.entities.*;

/**
 * 
 * Classe � part car le ship a ses propres contr�les
 * Elle n'h�rites pas d'autre classe
 *
 */
public class ShipManager {
	
	private Entity ship;
	private double moveSpeed = 300; // Vitesse de d�placement du vaisseau
	private long lastFire = 0; //Dernier tir du vaisseau
	private long fireInterval = 200; //Intervalle de temps pour lequel le vaisseau peut tirer
	private ShootManager shootManager  = new ShootManager();
	
	public ShipManager(Game game, EntitiesManager eM) {
		ship = new EntityShip(game, "sprites/ship.gif", 370, 550, eM); //Cr�ation d'un vaisseau et insertion dans la gestion des entit�s (collision etc...)
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
	public Entity getShip() {
		return ship;
	}
	
	//Voir si le vaisseau peut tirer
	public void tryToShoot(Long actualTime, EntitiesManager eM, Game g) {
		if(actualTime - lastFire < fireInterval) return; //L'interval de tir est trop court
		
		lastFire = actualTime;
		eM.getEntitiesList().add(shootManager.getShoot(g, eM, ship.getX(), ship.getY(), 0));
	}
}
