package fr.univ_artois.iut_lens.spaceinvader.entities.ennemy_move_strategy;

import java.util.Random;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.EntityEnnemy;

public class StrategyMoveEnnemyUltimatePowerOfGodEndOfTheWorldOhMyGoshShutTheFuckUpThisIsTheEnd extends StrategyMoveEnnemy {
	
	private int countOfDeath;
	
	@Override
	public void performMove(long delta, EntitiesManager entMan) {
		Random r = new Random();
		countOfDeath++;
		
		
		for(Entity entity : entMan.getEntitiesList()) {
	    	if (entity instanceof EntityEnnemy){
	    		// direction courante de cet EntityEnnemy
	    		char leftRight = (entity.getSpeed().getX() < 0)?'l':'r';
	    		char upDown = (r.nextInt(50)<=3)?'u':'d';
	    		
	    		char newLeftRight = leftRight;
	    		char newUpDown = upDown;
	    		
	    		
	    		// if we have reached the left hand side of the screen and
	    		// are moving left then request a logic update 
	    		if (leftRight == 'l' && (entity.getPosition().getX() < 10))
	    			leftRight = 'r';
	    		// and vice vesa, if we have reached the right hand side of 
	    		// the screen and are moving right, request a logic update
	    		else if (leftRight == 'r' && (entity.getPosition().getX() > 750))
	    			leftRight= 'l';
	    		// sinon, on essaye de lui faire changer de direction, si on a de la chance
	    		else if (r.nextInt(50)<=5) // 1 chance sur 50
	    			leftRight = (leftRight == 'l')?'r':'l';
	    		
	    		if(upDown == 'd' && (entity.getPosition().getY()>570))
	    			upDown = 'u';
	    		else if(upDown=='u' && (entity.getPosition().getY()<30))
	    			upDown = 'd';
	    			
	    		
	    		// change la direction si nécessaire
	    		if (newLeftRight != leftRight)
		    		entity.getSpeed().setX(-entity.getSpeed().getX());
	    		if(newUpDown != upDown)
	    			entity.getSpeed().setY(-entity.getSpeed().getY());
	    		
	    		
	    		entity.move(delta); // applique le speed à la position actuelle (commun à tout les Entity)
	    	}
		}
		// fait un gros bordel
		if (countOfDeath>10) {
			countOfDeath = 0;
			for(Entity entity : entMan.getEntitiesList()) {
				if(entity instanceof EntityEnnemy) {
					entity.getSpeed().setX(entity.getSpeed().getX()+10);
					entity.getSpeed().setY(entity.getSpeed().getY()+10);
				}
				
			}
			
		}
	}
}
