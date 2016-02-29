package fr.univ_artois.iut_lens.spaceinvader.server.entities;

import java.awt.Rectangle;
import java.util.concurrent.atomic.AtomicInteger;

import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap.MapData.EntityDataSpawn;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap.MapData.EntityDataUpdated;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.shot.EntityShot;
import fr.univ_artois.iut_lens.spaceinvader.sprites_manager.Sprite;
import fr.univ_artois.iut_lens.spaceinvader.sprites_manager.SpriteStore;
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
	
	private Vector2d oldSpeed = new Vector2d();
	
	/** Points de vie restants pour l'entité courante. Par défaut, tué en un coup */
	protected int life = 1;
	
	protected int maxLife = life;
	
	protected EntitiesManager entitiesManager;
	
	private boolean plannedToRemoved = false;
	
	private static AtomicInteger idCounter = new AtomicInteger(0);
	public final int id;
	
	private String displayName = null;
	
	public boolean plannedToRemoved() {
		 return plannedToRemoved;
	}
	
	public void planToRemove() {
		plannedToRemoved = true;
		willDie();
	}
	public void dontRemove() { plannedToRemoved = false; }
	
	
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
		id = idCounter.getAndIncrement();
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
		final double rayon = Math.min(sprite.getWidth()/2D, sprite.getHeight()/2D);
		return rayon;
	}
	
	/**
	 * Notification that this entity collided with another.
	 * 
	 * @param other The entity with which this entity collided.
	 */
	public abstract void collidedWith(Entity other);
	
	
	
	/**
	 * Notification that this entity will die.
	 * Default implementation do nothing.
	 */
	public void willDie() {
		// do nothing
	}
	
	
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
			entitiesManager.remove(this);
			return true;
		}
		return false;
	}
	
	public Camp getCamp() {
		return Camp.NEUTRAL;
	}
	
	
	
	public EntityDataSpawn getEntityDataSpawn() {
		EntityDataSpawn eData = new EntityDataSpawn();
		eData.id = id;
		eData.currentLife = (getMaxLife() > 1) ? getLife() : 0;
		eData.maxLife = (getMaxLife() > 1) ? getMaxLife() : 0;
		eData.name = (getDisplayName() != null) ? getDisplayName() : "";
		eData.spriteId = getSprite().id;
		eData.posX = (float)position.x;
		eData.posY = (float)position.y;
		eData.speedX = (float)speed.x;
		eData.speedY = (float)speed.y;
		return eData;
	}
	
	
	public EntityDataUpdated getEntityDataUpdated() {
		EntityDataUpdated eData = new EntityDataUpdated();
		eData.id = id;
		eData.currentLife = (getMaxLife() > 1) ? getLife() : 0;
		eData.posX = (float)position.x;
		eData.posY = (float)position.y;
		eData.speedX = (float)speed.x;
		eData.speedY = (float)speed.y;
		return eData;
	}
	
	
	
	public Vector2d getPosition() { return position; }
	
	
	public Vector2d getSpeed() { return speed; }
	public boolean hasChangedSpeed() {
		return !oldSpeed.equals(speed);
	}
	public void resetOldSpeed() { oldSpeed = new Vector2d(speed); }
	
	public void setPosition(Vector2d newPos) { position = newPos; }
	public void setSpeed(Vector2d newSpeed) { speed = newSpeed; }
	public int getLife() { return life; }
	public int getMaxLife() { return maxLife; }
	public Sprite getSprite() { return sprite; }
	
	public void setDisplayName(String n) { displayName = n; }
	public String getDisplayName() { return displayName; }
	
	
	
	public static enum Camp {
		ALLY, NEUTRAL, ENEMY
	}
}