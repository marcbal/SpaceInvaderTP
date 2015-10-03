package fr.univ_artois.iut_lens.spaceinvader.entities.ennemy;

import java.awt.Color;
import java.awt.Graphics;

import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
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
		speed.x = -moveSpeed;
		
		life = l;
		maxLife = life;
	}

	/**
	 * Request that this alien moved based on time elapsed
	 * 
	 * @param delta The time that has elapsed since last move
	 */
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
	public synchronized void collidedWith(Entity other) {
		// collisions with aliens are handled elsewhere
	}
	
	
	
	
	@Override
	public void setNotifyAlienKilled()
	{
		speed.x *= 1.02;
	}
	

	@Override
	public Camp getCamp() {
		return Camp.ENEMY;
	}
	

	
	/**
	 * Draw this entity to the graphics context provided
	 * 
	 * @param g The graphics context on which to draw
	 */
	public void draw(Graphics g) {
		int life = this.life; // évite les problèmes de multithreading et bug d'affichage
		super.draw(g);
		if (life==maxLife || life <= 0)
			return;
		g.setColor(Color.DARK_GRAY);
		g.fillRect((int)position.x,(int)position.y, sprite.getWidth(), 3);
		g.setColor(new Color((float)Math.sqrt(1-(life/(float)maxLife)), (float)Math.sqrt(life/(float)maxLife), 0F));
		g.fillRect((int)position.x,(int)position.y, (int)((double)sprite.getWidth()*(life/(double)maxLife)), 3);
	}
	
	
}