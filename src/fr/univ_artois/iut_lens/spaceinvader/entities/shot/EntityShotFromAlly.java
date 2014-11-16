package fr.univ_artois.iut_lens.spaceinvader.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.Game;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

/**
 * An entity representing an Ally shot
 * @author Maxime
 *
 */
public abstract class EntityShotFromAlly  extends EntityShot{
	/** The vertical speed at which the players shot moves */
	
	/**
	 * Create a new shot from the player
	 * 
	 * @param sprite The sprite representing this shot
	 * @param x The initial x location of the shot
	 * @param y The initial y location of the shot
	 * @param d Degats
	 * @param s Speed vector
	 */
	public EntityShotFromAlly(String sprite,Vector2d p, int d, Vector2d s, EntitiesManager eM) {
		super(sprite,p,d,s,eM);
	}
	
	
	@Override
	public void collidedWith(Entity other) {
		// prevents double kills, if we've already hit something,
		// don't collide
		if (used) {
			return;
		}
		
		// if we've hit an alien, kill it!
		if (other instanceof EntityEnnemy) {
			
			// remove the affected entities
			entitiesManager.removeEntity(this);
			// other prends les dégats donnés par le tir
			if (other.receiveDegat(this))
				// notify the game that the alien has been killed
				Game.gameInstance.notifyAlienKilled();
				
				
			used = true;
		}
		//Si deux tirs se touchent
		if(other instanceof EntityShotFromEnnemy) {
			entitiesManager.removeEntity(this);
			entitiesManager.removeEntity(other);
			used = true;
		}
	}
}
