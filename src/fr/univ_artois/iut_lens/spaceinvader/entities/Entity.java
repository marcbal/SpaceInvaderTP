package fr.univ_artois.iut_lens.spaceinvader.entities;

import java.awt.Graphics;
import java.awt.Rectangle;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShot;
import fr.univ_artois.iut_lens.spaceinvader.Sprite;
import fr.univ_artois.iut_lens.spaceinvader.SpriteStore;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

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
	protected Vector2d position = new Vector2d();
	/** The sprite that represents this entity */
	protected Sprite sprite;
	/** The current speed of this entity horizontally (pixels/sec) */
	protected Vector2d speed = new Vector2d();
	/** Points de vie restants pour l'entité courante. Par défaut, tué en un coup */
	protected int life = 1;
	
	protected int maxLife = life;
	
	protected EntitiesManager entitiesManager;
	
	public boolean plannedToRemoved = false;
	
	/**
	 * Construct a entity based on a sprite image and a location.
	 * 
	 * @param ref The reference to the image to be displayed for this entity
 	 * @param x The initial x location of this entity
	 * @param y The initial y location of this entity
	 */
	public Entity(String ref,Vector2d p, EntitiesManager eM) {
		this.sprite = SpriteStore.get().getSprite(ref);
		position = p;
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
		position.x+=(delta * speed.x) / 1000000000;
		position.y+=(delta * speed.y) / 1000000000;
	}
	
	/**
	 * Draw this entity to the graphics context provided
	 * 
	 * @param g The graphics context on which to draw
	 */
	public void draw(Graphics g) {
		sprite.draw(g,(int)position.x,(int)position.y);
	}
	
	/**
	 * Check if this entity collised with another.
	 * 
	 * @param other The other entity to check collision against
	 * @return True if the entities collide with each other
	 */
	public boolean collidesWith(Entity other) {
		if (position.x + sprite.getWidth() < other.position.x
				|| position.x > other.position.x + other.sprite.getWidth()
				|| position.y + sprite.getHeight() < other.position.y
				|| position.y > other.position.y + other.sprite.getHeight()) return false;
		return true;
	}
	
	public Rectangle getBoundingBox()
	{
		return new Rectangle((int)position.x, (int)position.y, sprite.getWidth(), sprite.getHeight());
	}
	
	public Vector2d getCenter() {
		return new Vector2d(position.x + sprite.getWidth()/2D, position.y + sprite.getHeight()/2D);
	}
	
	/**
	 * Pour les sous classes implémentants l'interface CircleEntity.<br/>
	 * Donne le rayon de l'entité, en pixel. Si la texture est rectangulaire,
	 * la plus petite distance sera fourni par longueur et la largeur.
	 * @return
	 */
	public double getRayon() {
		return Math.min(sprite.getWidth()/2D, sprite.getHeight()/2D);
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
	

	public Vector2d getPosition() { return position; }
	public Vector2d getSpeed() { return speed; }
	public void setPosition(Vector2d newPos) { position = newPos; }
	public void setSpeed(Vector2d newSpeed) { speed = newSpeed; }
	public int getLife() { return life; }
}