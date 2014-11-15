package fr.univ_artois.iut_lens.spaceinvader;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;

/**
 * Cette classe sert juste à gérer les entitées
 *
 */
public class EntitiesManager {

	private List<Entity> entities = new ArrayList<Entity>();
	private List<Entity> removeList = new ArrayList<Entity>();
	
	private boolean logicRequiredThisLoop = false;
	
	
	
	//Méthode déssinant les entités
	public void draw(Graphics2D g) {
		// cycle round drawing all the entities we have in the game
        for(Entity entity : entities)
			entity.draw(g);
	}
	
	//Fonction permettant déplacer les entités
	public void moveEntities(long delta) {
		for(Entity entity : entities)
			entity.move(delta);
	}
	
	//Fonction agissant si un ennemi atteint un bord (par exemple)
	public void doEntitiesLogic() {
		if(logicRequiredThisLoop) {
		    for(Entity entity : entities) {
				entity.doLogic();
			}
			logicRequiredThisLoop = false;
		}
	}
	
	public void doCollisions() {
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
		// remove any entity that has been marked for clear up
		entities.removeAll(removeList);
		removeList.clear();
	}
	
	/**
	 * Notification from a game entity that the logic of the game
	 * should be run at the next opportunity (normally as a result of some
	 * game event)
	 */
	public void updateLogic() {
		logicRequiredThisLoop = true;
	}
	
	
	public List<Entity> getEntitiesList() { return entities; }
	
	
	/**
	 * Remove an entity from the game. The entity removed will
	 * no longer move or be drawn.
	 * 
	 * @param entity The entity that should be removed
	 */
	public void removeEntity(Entity entity) {
		removeList.add(entity);
	}
}
