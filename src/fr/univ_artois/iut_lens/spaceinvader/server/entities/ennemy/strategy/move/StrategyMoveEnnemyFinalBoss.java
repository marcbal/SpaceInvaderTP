package fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.move;

import java.util.Random;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class StrategyMoveEnnemyFinalBoss extends StrategyMoveEnnemy {
	
	private int countOfDeath;
	
	@Override
	public void performMove(long delta, EntitiesManager entMan) {
		Random r = MegaSpaceInvader.RANDOM;
		countOfDeath++;
		
		
		for(Entity entity : entMan.getEntityListSnapshot()) {
	    	if (entity instanceof EntityEnnemy){
	    		// direction courante de cet EntityEnnemy
	    		char leftRight = (entity.getSpeed().x < 0)?'l':'r';
	    		char upDown =    (entity.getSpeed().y > 0)?'d':'u';
	    		
	    		char newLeftRight = leftRight;
	    		char newUpDown = upDown;
	    		
	    		
	    		// if we have reached the left hand side of the screen and
	    		// are moving left then request a logic update 
	    		if (leftRight == 'l' && (entity.getPosition().x < 10))
	    			leftRight = 'r';
	    		// and vice vesa, if we have reached the right hand side of 
	    		// the screen and are moving right, request a logic update
	    		else if (leftRight == 'r' && (entity.getPosition().x > MegaSpaceInvader.DISPLAY_WIDTH - 10 - entity.getBoundingBox().width))
	    			leftRight= 'l';
	    		// sinon, on essaye de lui faire changer de direction, si on a de la chance
	    		else if (r.nextInt(1000)<=1) // 1 chance sur 10
	    			leftRight = (leftRight == 'l')?'r':'l';
	    		
	    		if(upDown == 'd' && (entity.getPosition().y > MegaSpaceInvader.DISPLAY_HEIGHT - 30 - entity.getBoundingBox().height))
	    			upDown = 'u';
	    		else if(upDown=='u' && (entity.getPosition().y < 30))
	    			upDown = 'd';
	    		else if(r.nextInt(1000)<=1)
	    			newUpDown = (upDown=='u')?'d':'u';
	    			
	    		
	    		// change la direction si nécessaire
	    		if (newLeftRight != leftRight)
		    		entity.setSpeed(entity.getSpeed().invertX());
	    		if(newUpDown != upDown)
	    			entity.setSpeed(entity.getSpeed().invertY());
	    		entity.getSpeed().y += 2 +(r.nextInt(
	    												Math.abs(570-(int)entity.getPosition().y)+1
	    										)/100F);
	    		
	    		entity.move(delta); // applique le speed à la position actuelle (commun à tout les Entity)
	    	}
		}
		// fait un gros bordel
		if (countOfDeath>10) {
			countOfDeath = 0;
			for(Entity entity : entMan.getEntityListSnapshot()) {
				if(entity instanceof EntityEnnemy) {
					entity.setSpeed(entity.getSpeed().add(new Vector2d(1, 1)));
				}
				
			}
			
		}
	}
}
