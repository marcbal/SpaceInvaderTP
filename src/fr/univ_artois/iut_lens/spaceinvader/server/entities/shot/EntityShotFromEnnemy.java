package fr.univ_artois.iut_lens.spaceinvader.server.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ship.EntityShip;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

/**
 * An entity representing a ennemy shot
 * @author Maxime
 *
 */
public class EntityShotFromEnnemy extends EntityShot {
	
	/**
	 * Create a new shot from the player
	 * 
	 * @param game The game in which the shot has been created
	 * @param sprite The sprite representing this shot
	 * @param x The initial x location of the shot
	 * @param y The initial y location of the shot
	 */
	public EntityShotFromEnnemy(String sprite, Vector2d p, int d, int l, Vector2d s,  EntitiesManager eM) {
		super(sprite, p, d, l, s, eM);
	}
	
	
	@Override
	public synchronized void collidedWith(Entity other) {
		
		// Si on a touché le vaisseau
		if (other instanceof EntityShip) {
			// géré dans le collidedWith() du vaisseau
		}
	}
	
	public void move(long delta) {
		// proceed with normal move
		super.move(delta);
		
		// if we shot off the screen, remove ourselfs
		if (position.y < -sprite.getHeight() || position.y > MegaSpaceInvader.DISPLAY_HEIGHT || position.x < -sprite.getWidth() || position.x > MegaSpaceInvader.DISPLAY_WIDTH) {
			entitiesManager.remove(this);
		}
	}

	@Override
	public Camp getCamp() {
		return Camp.ENEMY;
	}
}
