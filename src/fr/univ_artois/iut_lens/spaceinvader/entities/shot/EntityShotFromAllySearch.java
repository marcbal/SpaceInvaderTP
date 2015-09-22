package fr.univ_artois.iut_lens.spaceinvader.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.Game;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.util.TargettingUtil;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllySearch extends EntityShotFromAlly {
	
	private Entity target = null;
	
	private Vector2d acceleration = new Vector2d();

	private int maxSpeed = 500;
	private int minSpeed = 250;

	public EntityShotFromAllySearch(Vector2d p, Vector2d s, EntitiesManager eM) {
		super("sprites/shot_search.png", p, 20, 5, s, eM);
	}
	
	
	@Override
	public void move(long delta) {
		
		if (target == null || !entitiesManager.getEntitiesList().contains(target))
			searchEnnemy();
		
		if (target != null)
		{
			Vector2d vectDist = TargettingUtil.getTargetDirection(this, target);
			
			speed = vectDist.dotProduct(20);
			//acceleration = vectDist.dotProduct(maxSpeed/vectDist.distanceOf(new Vector2d())/50000);
			// System.out.println(acceleration.x+" "+acceleration.y);
		}
		
		

		if (speed.distanceSquaredOf(new Vector2d()) > maxSpeed*maxSpeed)
			speed = speed.dotProduct(maxSpeed/speed.distanceOf(new Vector2d()));
		if (speed.distanceSquaredOf(new Vector2d()) < minSpeed*minSpeed)
			speed = speed.dotProduct(minSpeed/speed.distanceOf(new Vector2d()));
		
		super.move(delta);
	}
	
	
	
	
	private void searchEnnemy() {
		double min = Double.MAX_VALUE;
		target = null;
		
		for (Entity e : entitiesManager.getEntitiesList())
		{
			if (!(e instanceof EntityEnnemy) && !(e instanceof EntityShotFromEnnemy)) continue;
			
			double distSq = position.distanceSquaredOf(e.getPosition());
			
			if (distSq < min) {
				min = distSq;
				target = e;
			}
		}
		
	}
	
	
}
