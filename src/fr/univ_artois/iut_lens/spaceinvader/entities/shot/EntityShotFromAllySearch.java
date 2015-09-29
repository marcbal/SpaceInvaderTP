package fr.univ_artois.iut_lens.spaceinvader.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.util.TargettingUtil;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllySearch extends EntityShotFromAlly {
	
	private Entity target = null;

	private int maxSpeed = 500;
	private int minSpeed = 250;

	public EntityShotFromAllySearch(Vector2d p, Vector2d s, EntitiesManager eM) {
		super("sprites/shot_search.png", p, 20, 5, s, eM);
	}
	
	
	@Override
	public void move(long delta) {
		
		if (target == null || !entitiesManager.getEntitiesList().contains(target))
			TargettingUtil.searchTargetEnnemy(this, entitiesManager);
		
		if (target != null)
		{
			Vector2d vectDist = TargettingUtil.getTargetDirection(this, target);
			
			speed = vectDist.dotProduct(20);
		}
		
		

		if (speed.distanceSquaredOf(new Vector2d()) > maxSpeed*maxSpeed)
			speed = speed.dotProduct(maxSpeed/speed.distanceOf(new Vector2d()));
		if (speed.distanceSquaredOf(new Vector2d()) < minSpeed*minSpeed)
			speed = speed.dotProduct(minSpeed/speed.distanceOf(new Vector2d()));
		
		super.move(delta);
	}
	
	
	
	
	
	
	
}
