package fr.univ_artois.iut_lens.spaceinvader.util;

import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;

public class TargettingUtil {
	
	/**
	 * Donne un représentant du vecteur partant du centre de shot vers le centre de target
	 * @param shot l'élément visant la cible
	 * @param target la cible visée
	 */
	public static Vector2d getTargetDirection(Entity shot, Entity target) {
		Vector2d targetPos = new Vector2d(target.getBoundingBox().getCenterX(),target.getBoundingBox().getCenterY());
		Vector2d actualPos = shot.getPosition().add(new Vector2d(shot.getBoundingBox().getWidth()/2D, shot.getBoundingBox().getHeight()/2D));
		return actualPos.invert().add(targetPos);
	}
	

}
