package fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.strategy.shot;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;

public abstract class StrategyShotEnnemy {
	
	public StrategyShotEnnemy() {
		
	}
	
	public abstract void performShot(EntitiesManager EntMan);
	
}
