package entities;

import base.EntitiesManager;
import base.Game;

/**
 * An entity representing a shot fired by the player's ship
 * 
 * @author Kevin Glass
 */
public abstract class EntityShot extends Entity {
	/** The vertical speed at which the players shot moves */
	private double moveSpeed;
	/** The game in which this entity exists */
	protected Game game;
	/** True if this shot has been "used", i.e. its hit something */
	protected boolean used = false;
	
	/**
	 * Create a new shot from the player
	 * 
	 * @param game The game in which the shot has been created
	 * @param sprite The sprite representing this shot
	 * @param x The initial x location of the shot
	 * @param y The initial y location of the shot
	 */
	public EntityShot(Game game,String sprite,int x,int y, int d, EntitiesManager eM) {
		super(sprite,x,y,eM);
		
		this.game = game;
		
		dy = moveSpeed;
		
		degat = d;
	}

	/**
	 * Request that this shot moved based on time elapsed
	 * 
	 * @param delta The time that has elapsed since last move
	 */
	public void move(long delta) {
		// proceed with normal move
		super.move(delta);
		
		// if we shot off the screen, remove ourselfs
		if (y < -100) {
			entitiesManager.removeEntity(this);
		}
	}
	
	/**
	 * Notification that this shot has collided with another
	 * entity
	 * 
	 * @parma other The other entity with which we've collided
	 */
	public void collidedWith(Entity other) {
		
	}

    @Override
    public void doLogic() {
        // FIXME Auto-generated method stub
        
    }
}