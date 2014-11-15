package fr.univ_artois.iut_lens.spaceinvader;

import fr.univ_artois.iut_lens.spaceinvader.entities.*;

/**
 * Cette classe gère tous les tirs
 *
 */
public class ShootManager {
	
	//Générer un tire
	public Entity getShoot(Game g, EntitiesManager eM, int x, int y, int i) {
		
		//tir normal
		if(i == 0) return new EntityShotFromAlly(g, "sprites/shot.gif", x, y, eM);
		else return null;
	}
}
