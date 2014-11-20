package fr.univ_artois.iut_lens.spaceinvader.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllySearch extends EntityShotFromAlly {
	
	private Entity target = null;
	
	private Vector2d acceleration = new Vector2d();
	
	private int maxSpeed = 500;

	public EntityShotFromAllySearch(Vector2d p, Vector2d s, EntitiesManager eM) {
		super("sprites/shot_search.png", p, 20, 5, s, eM);
	}
	
	
	@Override
	public void move(long delta) {
		
		if (target == null || !entitiesManager.getEntitiesList().contains(target))
			searchEnnemy();
		
		if (target != null)
		{
			Vector2d targetPos = new Vector2d(target.getBoundingBox().getCenterX(),target.getBoundingBox().getCenterY());
			Vector2d actualPos = position.dup().add(new Vector2d(getBoundingBox().getWidth()/2D, getBoundingBox().getHeight()/2D));
			Vector2d vectDist = actualPos.invert().add(targetPos);
			
			
			acceleration = vectDist.dotProduct(maxSpeed/vectDist.distanceOf(new Vector2d())/50000);
			// System.out.println(acceleration.x+" "+acceleration.y);
		}
		
		
		
		speed.add(acceleration.dotProduct(delta/1000D));
		if (speed.distanceSquaredOf(new Vector2d()) > maxSpeed*maxSpeed)
		speed.dotProduct(maxSpeed/speed.distanceOf(new Vector2d()));
		
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
