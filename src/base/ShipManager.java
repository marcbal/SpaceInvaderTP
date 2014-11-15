package base;

import entities.*;

/**
 * 
 * Classe à part car le ship a ses propres contrôles
 * Elle n'hérites pas d'autre classe
 *
 */
public class ShipManager {
	
	private Entity ship;
	private double moveSpeed = 300; // Vitesse de déplacement du vaisseau
	private long lastFire = 0; //Dernier tir du vaisseau
	private long fireInterval = 200; //Intervalle de temps pour lequel le vaisseau peut tirer
	
	public ShipManager(Game game, EntitiesManager eM) {
		ship = new EntityShip(game, "sprites/ship.gif", 370, 550, eM); //Création d'un vaisseau et insertion dans la gestion des entités (collision etc...)
	}
	
	public void moveShip(int i) {
		
		//Si la commande est -1 on va à gauche
		if(i == -1)
			ship.setHorizontalMovement(-moveSpeed);
		
		//Si la commande est 1  on va à droite
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
		eM.getEntitiesList().add(new EntityShotFromAlly(g,"sprites/shot.gif",ship.getX()+10,ship.getY()-30, 1, eM));
	}
}
