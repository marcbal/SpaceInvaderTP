package fr.univ_artois.iut_lens.spaceinvader.util;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity.Camp;

public class TargettingUtil {
	
	/**
	 * Donne un représentant du vecteur partant du centre de shot vers le centre de target
	 * @param shot l'élément visant la cible
	 * @param target la cible visée
	 */
	public static Vector2d getTargetDirection(Entity shot, Entity target) {
		return shot.getCenter().invert().add(target.getCenter());
	}
	
	
	public static <T extends Entity> T searchTargetEnnemy(Entity current, EntitiesManager entitiesManager, Class<T> targetType, boolean ignoreNeutral) {
		double min = Double.MAX_VALUE;
		T target = null;
		
		for (Entity e : entitiesManager.getEntityListSnapshot())
		{
			if (e == null) continue;
			if (!targetType.isInstance(e)) continue;
			if (current.getCamp() == e.getCamp()) continue;
			if (ignoreNeutral && e.getCamp() == Camp.NEUTRAL) continue;
			
			double distSq = current.getCenter().distanceSquaredOf(e.getCenter());
			
			if (distSq < min) {
				min = distSq;
				target = targetType.cast(e);
			}
		}
		return target;
		
	}

}
