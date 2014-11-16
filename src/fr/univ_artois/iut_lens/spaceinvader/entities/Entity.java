package fr.univ_artois.iut_lens.spaceinvader.entities;

import java.awt.Graphics;
import java.awt.Rectangle;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShot;
import fr.univ_artois.iut_lens.spaceinvader.Sprite;
import fr.univ_artois.iut_lens.spaceinvader.SpriteStore;
import fr.univ_artois.iut_lens.spaceinvader.util.Position;

/**
 * An entity represents any element that appears in the game. The
 * entity is responsible for resolving collisions and movement
 * based on a set of properties defined either by subclass or externally.
 * 
 * Note that doubles are used for positions. This may seem strange
 * given that pixels locations are integers. However, using double means
 * that an entity can move a partial pixel. It doesn't of course mean that
 * they will be display half way through a pixel but allows us not lose
 * accuracy as we move.
 * 
 * @author Kevin Glass
 */
public abstract class Entity {
	/** The current x location of this entity */ 
	protected Position position = new Position();
	/** The sprite that represents this entity */
	protected Sprite sprite;
	/** The current speed of this entity horizontally (pixels/sec) */
	protected Position speed = new Position();
	/** Points de vie restants pour l'entité courante. Par défaut, tué en un coup */
	protected int life = 1;
	
	protected EntitiesManager entitiesManager;
	
	/**
	 * Construct a entity based on a sprite image and a location.
	 * 
	 * @param ref The reference to the image to be displayed for this entity
 	 * @param x The initial x location of this entity
	 * @param y The initial y location of this entity
	 */
	public Entity(String ref,double x,double y, EntitiesManager eM) {
		this.sprite = SpriteStore.get().getSprite(ref);
		position = new Position(x, y);
		entitiesManager = eM;
	}
	
	/**
	 * Request that this entity move itself based on a certain ammount
	 * of time passing.
	 * 
	 * @param delta The ammount of time that has passed in milliseconds
	 */
	public void move(long delta) {
		// update the location of the entity based on move speeds
		position.setX(position.getX()+((delta * speed.getX()) / 1000000000));
		position.setY(position.getY()+((delta * speed.getY()) / 1000000000));
	}
	
	/**
	 * Set the horizontal speed of this entity
	 * 
	 * @param dx The horizontal speed of this entity (pixels/sec)
	 */
	public void setHorizontalMovement(double dx) {
		speed.setX(dx);
	}

	/**
	 * Set the vertical speed of this entity
	 * 
	 * @param dx The vertical speed of this entity (pixels/sec)
	 */
	public void setVerticalMovement(double dy) {
		speed.setY((int)dy);
	}
	
	/**
	 * Get the horizontal speed of this entity
	 * 
	 * @return The horizontal speed of this entity (pixels/sec)
	 */
	public double getHorizontalMovement() {
		return speed.getX();
	}

	/**
	 * Get the vertical speed of this entity
	 * 
	 * @return The vertical speed of this entity (pixels/sec)
	 */
	public double getVerticalMovement() {
		return speed.getY();
	}
	
	/**
	 * Draw this entity to the graphics context provided
	 * 
	 * @param g The graphics context on which to draw
	 */
	public void draw(Graphics g) {
		sprite.draw(g,(int)position.getX(),(int)position.getY());
	}
	
	/**
	 * Get the x location of this entity
	 * 
	 * @return The x location of this entity
	 */
	public double getX() {
		return position.getX();
	}

	/**
	 * Get the y location of this entity
	 * 
	 * @return The y location of this entity
	 */
	public double getY() {
		return position.getY();
	}
	
	/**
	 * Check if this entity collised with another.
	 * 
	 * @param other The other entity to check collision against
	 * @return True if the entities collide with each other
	 */
	public boolean collidesWith(Entity other) {
		return getBoundingBox().intersects(other.getBoundingBox());
	}
	
	public Rectangle getBoundingBox()
	{
		return new Rectangle((int)position.getX(), (int)position.getY(), sprite.getWidth(), sprite.getHeight());
	}
	
	/**
	 * Notification that this entity collided with another.
	 * 
	 * @param other The entity with which this entity collided.
	 */
	public abstract void collidedWith(Entity other);
	
	
	/**
	 * Prends en charge l'évènement lorsqu'une entité se fait tuer
	 * 
	 */
	
	public void setNotifyAlienKilled()
	{
		// rien
		// implémenté dans une sous-classe si nécessaire
	}
	
	
	public boolean receiveDegat(EntityShot shot)
	{
		if (life == 0) // c'est le cas si this est un tir
			return false;
		
		life -= shot.getDegat();
		if (life <= 0)
		{
			entitiesManager.removeEntity(this);
			return true;
		}
		return false;
	}
	
	
	public Position getPosition() { return position; }
	public Position getSpeed() { return speed; }
	
	
	/**
	 * Une entité a un nom, utile pour savoir si une collision viens d'un tire amis ou non
	 * @return
	 */
}