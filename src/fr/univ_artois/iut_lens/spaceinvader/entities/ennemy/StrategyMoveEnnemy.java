package fr.univ_artois.iut_lens.spaceinvader.entities.ennemy;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;

public abstract class StrategyMoveEnnemy {
	
	
	public StrategyMoveEnnemy() {
	}
	
	
	public abstract void performMove(long delta, EntitiesManager entMan);
	
	
	
}
