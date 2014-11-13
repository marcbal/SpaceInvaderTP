package base;

import java.awt.*;
import java.util.ArrayList;

import entities.Entity;

/**
 * Cette classe sert juste à gérer les entitées
 *
 */
public class EntitiesManager {
	
	//Méthode déssinant les entités
	public void draw(ArrayList<Entity> entities, Graphics2D g) {
		// cycle round drawing all the entities we have in the game
        for(Entity entity : entities)
			entity.draw(g);
	}
	
	//Fonction permettant déplacer les entités
	public void moveEntity(ArrayList<Entity> entities, long delta) {
		for(Entity entity : entities)
			entity.move(delta);
	}
	
	//Fonction agissant si un ennemi atteint un bord (par exemple)
	public void doEntityLogic(ArrayList<Entity> entities, boolean logicRequiredThisLoop) {
	    for(Entity entity : entities) {
			entity.doLogic();
		}
	}
	
	public void collisionChecker(ArrayList<Entity> entities, ArrayList<Entity> removeList) {
		// brute force collisions, compare every entity against
		// every other entity. If any of them collide notify 
		// both entities that the collision has occured
		for (int p=0;p<entities.size();p++) {
			for (int s=p+1;s<entities.size();s++) {
				Entity me = entities.get(p);
				Entity him = entities.get(s);
				if (removeList.contains(me) || removeList.contains(him)) continue;
				
				if (me.collidesWith(him)) {
					me.collidedWith(him);
					him.collidedWith(me);
				}
			}
		}
	}
	
	public void removeCollided(ArrayList<Entity> entities, ArrayList<Entity> removeList) {
		// remove any entity that has been marked for clear up
		entities.removeAll(removeList);
		removeList.clear();
	}
}
