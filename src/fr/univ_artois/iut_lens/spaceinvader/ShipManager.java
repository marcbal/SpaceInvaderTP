package fr.univ_artois.iut_lens.spaceinvader;

import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.EntityShip;

/**
 * 
 * Classe ï¿½ part car le ship a ses propres contrï¿½les
 * Elle n'hï¿½rites pas d'autre classe
 *
 */
public class ShipManager {
	
	private Entity ship;
<<<<<<< HEAD:src/base/ShipManager.java
	private double moveSpeed = 300; // Vitesse de déplacement du vaisseau
	private long lastFire = 0; //Dernier tir du vaisseau
	private long fireInterval = 200; //Intervalle de temps pour lequel le vaisseau peut tirer
=======
	private double moveSpeed = 300; // Vitesse de dï¿½placement du vaisseau
>>>>>>> origin/master:src/fr/univ_artois/iut_lens/spaceinvader/ShipManager.java
	
	public ShipManager(Game game, EntitiesManager eM) {
		ship = new EntityShip(game, "sprites/ship.gif", 370, 550, eM); //Crï¿½ation d'un vaisseau et insertion dans la gestion des entitï¿½s (collision etc...)
	}
	
	public void moveShip(int i) {
		
		//Si la commande est -1 on va ï¿½ gauche
		if(i == -1)
			ship.setHorizontalMovement(-moveSpeed);
		
		//Si la commande est 1  on va ï¿½ droite
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
