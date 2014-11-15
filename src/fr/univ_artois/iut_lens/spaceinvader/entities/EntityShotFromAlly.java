package fr.univ_artois.iut_lens.spaceinvader.entities;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.Game;

/**
 * An entity representing an Ally shot
 * @author Maxime
 *
 */
public class EntityShotFromAlly  extends EntityShot{
	/** The vertical speed at which the players shot moves */
	private double moveSpeed = -3000;
	
	/**
	 * Create a new shot from the player
	 * 
	 * @param game The game in which the shot has been created
	 * @param sprite The sprite representing this shot
	 * @param x The initial x location of the shot
	 * @param y The initial y location of the shot
	 */
	public EntityShotFromAlly(Game game,String sprite,int x,int y, EntitiesManager eM) {
		super(game, sprite,x,y,1,eM);
		
		dy = moveSpeed;
	}
	
	
	@Override
	public void collidedWith(Entity other) {
		// prevents double kills, if we've already hit something,
		// don't collide
		if (used) {
			return;
		}
		
		// if we've hit an alien, kill it!
		if (other instanceof EntityEnnemy) {
			
			// remove the affected entities
			entitiesManager.removeEntity(this);
			// other prends les dégats donnés par le tir
			if (other.receiveDegat(this))
				// notify the game that the alien has been killed
				game.notifyAlienKilled();
			used = true;
		}
	}
}
