package fr.univ_artois.iut_lens.spaceinvader.server.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ship.EntityShip;
import fr.univ_artois.iut_lens.spaceinvader.util.TargettingUtil;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllySearch extends EntityShotFromAlly {
	
	private Entity target = null;

	private int maxSpeed = 500;
	private int minSpeed = 250;

	public EntityShotFromAllySearch(Vector2d p, Vector2d s, EntitiesManager eM, EntityShip ship) {
		super("sprites/shot_search.png", p, 20, 5, s, eM, ship);
	}
	
	
	@Override
	public void move(long delta) {
		
		if (target == null || target.plannedToRemoved())
			target = TargettingUtil.searchTargetEnnemy(this, entitiesManager, Entity.class, true);
		
		if (target != null)
		{
			Vector2d vectDist = TargettingUtil.getTargetDirection(this, target);
			
			speed = vectDist.dotProduct(20);
		}
		
		
		speed = speed.minLength(minSpeed).maxLength(maxSpeed);
		
		super.move(delta);
	}
	
	
	
	
	
	
	
}
