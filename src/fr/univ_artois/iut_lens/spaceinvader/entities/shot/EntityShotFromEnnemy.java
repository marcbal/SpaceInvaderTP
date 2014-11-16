package fr.univ_artois.iut_lens.spaceinvader.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.Game;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ship.EntityShip;
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
	public EntityShotFromEnnemy(Game game,String sprite,Vector2d p, Vector2d s, EntitiesManager eM) {
		super(sprite,p,1,s,eM);
	}
	
	
	@Override
	public void collidedWith(Entity other) {
		// prevents double kills, if we've already hit something,
		// don't collide
		if (used) {
			return;
		}
		
		// if we've hit an alien, kill it!
		if (other instanceof EntityShip) {
			// remove the affected entities
			entitiesManager.removeEntity(this);
			// other prends les dégats donnés par le tir
			if (other.receiveDegat(this))
				// notify the game that the alien has been killed
				Game.gameInstance.notifyAlienKilled();
			
			used = true;
		}
	}
}
