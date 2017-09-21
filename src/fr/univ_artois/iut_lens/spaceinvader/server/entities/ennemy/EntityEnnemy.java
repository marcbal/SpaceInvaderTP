package fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

/**
 * An entity which represents one of our space invader aliens.
 * 
 * @author Kevin Glass
 */
public class EntityEnnemy extends Entity {
	/** The speed at which the alient moves horizontally */
	private double moveSpeed = 75;
	
	/**
	 * Create a new alien entity
	 * 
	 * @param game The game in which this entity is being created
	 * @param ref The sprite which should be displayed for this alien
	 * @param x The intial x location of this alien
	 * @param y The intial y location of this alient
	 */
	public EntityEnnemy(String ref,Vector2d p, int l, EntitiesManager eM) {
		super(ref,p,eM);
		getSpeed().x = -moveSpeed;
		
		life = l;
		maxLife = life;
	}

	/**
	 * Request that this alien moved based on time elapsed
	 * 
	 * @param delta The time that has elapsed since last move
	 */
	@Override
	public void move(long delta) {
		// les mouvements sont calculés dans le strategyMoveEnnemy;
		
		// proceed with normal move
		super.move(delta);
	}
	
	/**
	 * Notification that this alien has collided with another entity
	 * 
	 * @param other The other entity
	 */
	@Override
	public synchronized void collidedWith(Entity other) {
		// collisions with aliens are handled elsewhere
	}
	
	
	
	
	@Override
	public void setNotifyAlienKilled()
	{
		getSpeed().x *= 1.02;
	}
	

	@Override
	public Camp getCamp() {
		return Camp.ENEMY;
	}
	
	
}