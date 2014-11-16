package fr.univ_artois.iut_lens.spaceinvader.entities.ship;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.Game;
import fr.univ_artois.iut_lens.spaceinvader.SpriteStore;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShotFromEnnemy;

/**
 * The entity that represents the players ship
 * 
 * @author Kevin Glass
 */
public abstract class EntityShip extends Entity {
	
	/**
	 * Create a new entity to represent the players ship
	 *  
	 * @param gameInstance The game in which the ship is being created
	 * @param ref The reference to the sprite to show for the ship
	 * @param x The initial x location of the player's ship
	 * @param y The initial y location of the player's ship
	 */
	public EntityShip(String ref, EntitiesManager eM) {
		super(ref,
				800/2-SpriteStore.get().getSprite(ref).getWidth()/2,
				600-SpriteStore.get().getSprite(ref).getHeight(),
				eM);
	}
	
	/**
	 * Request that the ship move itself based on an elapsed ammount of
	 * time
	 * 
	 * @param delta The time that has elapsed since last move (ms)
	 */
	public void move(long delta) {
		// if we're moving left and have reached the left hand side
		// of the screen, don't move
		if ((speed.getX() < 0) && (position.getX() < 10)) {
			return;
		}
		// if we're moving right and have reached the right hand side
		// of the screen, don't move
		if ((speed.getX() > 0) && (position.getX() > 750)) {
			return;
		}
		
		super.move(delta);
	}
	
	/**
	 * Notification that the player's ship has collided with something
	 * 
	 * @param other The entity with which the ship has collided
	 */
	public void collidedWith(Entity other) {
		// if its an alien or a shoot, notify the game that the player
		// is dead
		if (other instanceof EntityEnnemy || other instanceof EntityShotFromEnnemy) {
			Game.gameInstance.notifyDeath();
		}
	}
}