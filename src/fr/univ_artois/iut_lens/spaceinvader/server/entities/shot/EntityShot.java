package fr.univ_artois.iut_lens.spaceinvader.server.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

/**
 * An entity representing a shot fired by the player's ship
 * 
 * @author Kevin Glass
 */
public abstract class EntityShot extends Entity {
	/** Points de dégat */
	protected int degat = 0;
	
	/**
	 * Create a new shot from the player
	 * 
	 * @param game The game in which the shot has been created
	 * @param sprite The sprite representing this shot
	 * @param x The initial x location of the shot
	 * @param y The initial y location of the shot
	 */
	public EntityShot(String sprite,Vector2d p, int d, int l, Vector2d s, EntitiesManager eM) {
		super(sprite,p,eM);
		
		life = l;
		
		degat = d;
		
		setSpeed(s);
	}
	
	
	
	public int getDegat() { return degat; }
}