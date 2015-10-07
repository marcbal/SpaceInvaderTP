package fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.move;

import java.util.Random;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.Server;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;

public class StrategyMoveEnnemyRandom extends StrategyMoveEnnemy {

	private long frameCount = 0;
	
	@Override
	public void performMove(long delta, EntitiesManager entMan) {

		frameCount++;
		
		Random r = new Random();
		
		
		for(Entity entity : entMan.getEntityListSnapshot())
	    	if (entity instanceof EntityEnnemy)
	    	{
	    		// direction courante de cet EntityEnnemy
	    		char direction = (entity.getSpeed().x < 0)?'l':'r';
	    		char newDirection = direction;
	    		
	    		
	    		// if we have reached the left hand side of the screen and
	    		// are moving left then request a logic update 
	    		if (direction == 'l' && (entity.getPosition().x < 10))
	    			newDirection = 'r';
	    		// and vice vesa, if we have reached the right hand side of 
	    		// the screen and are moving right, request a logic update
	    		else if (direction == 'r' && (entity.getPosition().x > MegaSpaceInvader.DISPLAY_WIDTH - 10 - entity.getBoundingBox().width))
	    			newDirection = 'l';
	    		// sinon, on essaye de lui faire changer de direction, si on a de la chance
	    		else if (r.nextInt(50)==0) // 1 chance sur 50
	    			newDirection = (direction == 'l')?'r':'l';
	    		
	    		
	    		
	    		// change la direction si nécessaire
	    		if (newDirection != direction)
		    		entity.setSpeed(entity.getSpeed().invertX());
	    		
	    		
	    		entity.move(delta); // applique le speed à la position actuelle (commun à tout les Entity)
	    		
	    		// fait descendre les aliens un coup de temps en temps
	    		if (frameCount%500==0)
		    		entity.getPosition().y += 10;
	    		
	    		// si un des aliens touche en bas
	    		if (entity.getPosition().y > MegaSpaceInvader.DISPLAY_HEIGHT - 10 - entity.getBoundingBox().height)
	    			Server.serverInstance.finishLevel(false);
	    		
	    	}
		
		
	}

}
