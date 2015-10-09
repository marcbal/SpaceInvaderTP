package fr.univ_artois.iut_lens.spaceinvader.server.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.Server;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;
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
	public EntityShotFromAlly(String sprite,Vector2d p, int d, int l, Vector2d s, EntitiesManager eM) {
		super(sprite,p,d,l,s,eM);
	}
	
	
	@Override
	public synchronized void collidedWith(Entity other) {
		// prevents double kills, if we've already hit something,
		// don't collide
		if (used) {
			return;
		}
		
		// if we've hit an alien, kill it!
		if (other instanceof EntityEnnemy) {
			
			// remove the affected entities
			entitiesManager.remove(this);
			// other prends les dégats donnés par le tir
			if (other.receiveDegat(this))
				// notify the game that the alien has been killed
				Server.serverInstance.notifyAlienKilled();
				
				
			used = true;
		}

		//Si deux tirs se touchent (les 2 tirs dans les camps différents)
		if(other instanceof EntityShotFromEnnemy) {
			if (receiveDegat((EntityShot) other))
			{
				entitiesManager.remove(this);
				// la suite sers car en appelant entitiesManager.removeEntity(),
				// on ne calcule pas le retrait de point de vie de l'autre tir
				if(other.receiveDegat((EntityShot) this))
					entitiesManager.remove(other);
			}
		}
	}
	public void move(long delta) {
		// proceed with normal move
		super.move(delta);
		
		// if we shot off the screen, remove ourselfs
		if (position.y < -50 || position.y > MegaSpaceInvader.DISPLAY_HEIGHT + 50 || position.x < -50 || position.x > MegaSpaceInvader.DISPLAY_WIDTH + 50) {
			entitiesManager.remove(this);
		}
	}
	
	@Override
	public Camp getCamp() {
		return Camp.ALLY;
	}
}
