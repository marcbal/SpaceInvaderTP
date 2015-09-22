package fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.strategy.move;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.Game;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.EntityEnnemy;

public class StrategyMoveEnnemyNormal extends StrategyMoveEnnemy {

	@Override
	public void performMove(long delta, EntitiesManager entMan) {
		
		boolean changerDirection = false;
		
		
		for(Entity entity : entMan.getEntitiesList())
	    	if (entity instanceof EntityEnnemy)
	    	{
	    		
	    		// if we have reached the left hand side of the screen and
	    		// are moving left then request a logic update 
	    		if ((entity.getSpeed().x < 0) && (entity.getPosition().x < 10)) {
	    			changerDirection = true;
	    		}
	    		// and vice vesa, if we have reached the right hand side of 
	    		// the screen and are moving right, request a logic update
	    		if ((entity.getSpeed().x > 0) && (entity.getPosition().x > Game.gameInstance.getWindowWidth() - 10 - entity.getBoundingBox().width)) {
	    			changerDirection = true;
	    		}
	    		
	    		entity.move(delta); // applique le speed à la position actuelle (commun à tout les Entity)
	    	}
		
		
		if(changerDirection) {
		    for(Entity entity : entMan.getEntitiesList().toArray(new Entity[entMan.getEntitiesList().size()])) {
		    	if (entity instanceof EntityEnnemy)
		    	{
		    		// swap over horizontal movement and move down the
		    		// screen a bit
		    		entity.setSpeed(entity.getSpeed().invertX());
		    		entity.getPosition().y+=10;
		    		
		    		// if we've reached the bottom of the screen then the player
		    		// dies
		    		if (entity.getPosition().y > Game.gameInstance.getWindowHeight() - entity.getBoundingBox().height) {
		    			Game.gameInstance.notifyDeath();
		    		}
		    	}
			}
		    changerDirection = false;
		}
		
		
	}

}
