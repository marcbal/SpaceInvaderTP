package fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.move;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.Server;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;

public class StrategyMoveEnnemyNormal extends StrategyMoveEnnemy {

	@Override
	public void performMove(long delta, EntitiesManager entMan) {
		
		boolean changerDirection = false;
		
		
		for(Entity entity : entMan.getEntityListSnapshot())
	    	if (entity instanceof EntityEnnemy)
	    	{
	    		
	    		// if we have reached the left hand side of the screen and
	    		// are moving left then request a logic update 
	    		if ((entity.getSpeed().x < 0) && (entity.getPosition().x < 10)) {
	    			changerDirection = true;
	    		}
	    		// and vice vesa, if we have reached the right hand side of 
	    		// the screen and are moving right, request a logic update
	    		if ((entity.getSpeed().x > 0) && (entity.getPosition().x > MegaSpaceInvader.DISPLAY_WIDTH - 10 - entity.getBoundingBox().width)) {
	    			changerDirection = true;
	    		}
	    		
	    		entity.move(delta); // applique le speed à la position actuelle (commun à tout les Entity)
	    	}
		
		
		if(changerDirection) {
		    for(Entity entity : entMan.getEntityListSnapshot()) {
		    	if (entity instanceof EntityEnnemy)
		    	{
		    		// swap over horizontal movement and move down the
		    		// screen a bit
		    		entity.setSpeed(entity.getSpeed().invertX());
		    		entity.getPosition().y+=10;
		    		
		    		// if we've reached the bottom of the screen then the player
		    		// dies
		    		if (entity.getPosition().y > MegaSpaceInvader.DISPLAY_HEIGHT - entity.getBoundingBox().height) {
		    			Server.serverInstance.finishLevel(false);
		    		}
		    	}
			}
		    changerDirection = false;
		}
		
		
	}

}
