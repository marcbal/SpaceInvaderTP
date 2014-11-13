package entities;

import base.Game;

/**
 * An entity representing an Ally shot
 * @author Maxime
 *
 */
public class AllyShotEntity  extends ShotEntity{
	/** The vertical speed at which the players shot moves */
	private double moveSpeed = -300;
	
	/**
	 * Create a new shot from the player
	 * 
	 * @param game The game in which the shot has been created
	 * @param sprite The sprite representing this shot
	 * @param x The initial x location of the shot
	 * @param y The initial y location of the shot
	 */
	public AllyShotEntity(Game game,String sprite,int x,int y, int d) {
		super(game, sprite,x,y,d);
		
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
		if (other instanceof AlienEntity) {
			
			// remove the affected entities
			game.removeEntity(this);
			// other prends les dégats donnés par le tir
			if (other.receiveDegat(this, game))
				// notify the game that the alien has been killed
				game.notifyAlienKilled();
			used = true;
		}
	}
}
