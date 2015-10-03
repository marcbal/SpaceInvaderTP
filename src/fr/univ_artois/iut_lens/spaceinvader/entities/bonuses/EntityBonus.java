package fr.univ_artois.iut_lens.spaceinvader.entities.bonuses;

import fr.univ_artois.iut_lens.spaceinvader.Game;
import fr.univ_artois.iut_lens.spaceinvader.ShipManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

/**
 * An entity representing a shot fired by the player's ship
 * 
 * @author Kevin Glass
 */
public abstract class EntityBonus extends Entity {
	/** True if this shot has been "used", i.e. its hit something */
	protected boolean used = false;
	protected ShipManager shipManager;
	
	/**
	 * Create a new shot from the player
	 * 
	 * @param game The game in which the shot has been created
	 * @param sprite The sprite representing this shot
	 * @param x The initial x location of the shot
	 * @param y The initial y location of the shot
	 */
	public EntityBonus(String sprite,Vector2d p, Vector2d s, EntitiesManager eM, ShipManager sM) {
		super(sprite,p,eM);
		speed = s;
		shipManager = sM;
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
		if (position.y > Game.gameInstance.getWindowHeight()) {
			entitiesManager.removeEntity(this);
		}
	}
}
