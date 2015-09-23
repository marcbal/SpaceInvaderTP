package fr.univ_artois.iut_lens.spaceinvader.entities;

import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public interface CircleEntity {
	public Vector2d getCenter();
	
	public double getRayon();
	
	
	public static boolean collides(CircleEntity e1, CircleEntity e2) {
		double dist = e1.getRayon()+e2.getRayon();
		return (e1.getCenter().distanceSquaredOf(e2.getCenter()) < dist*dist);
	}
}
