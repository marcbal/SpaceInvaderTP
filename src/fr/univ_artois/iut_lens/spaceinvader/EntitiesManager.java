package fr.univ_artois.iut_lens.spaceinvader;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.EntityEnnemy;
/**
 * Cette classe sert juste à gérer les entitées
 *
 */
public class EntitiesManager {

	private List<Entity> entities = new ArrayList<Entity>();
	private List<Entity> removeList = new ArrayList<Entity>();
	
	private Entity[] entitiesMT; // utilisé pour les calculs en multiThread (plus optimisé)
	
	private int nbThread = Runtime.getRuntime().availableProcessors();
	
	private CollisionThread[] threadsRunnable = new CollisionThread[nbThread];
	
	private ExecutorService threadpool;
	
	private int lastCollisionComputingNumber = 0;
	
	
	
	public EntitiesManager() {

		
		for (int cTh=0; cTh<nbThread; cTh++)
		{
			threadsRunnable[cTh] = new CollisionThread(cTh, nbThread);
		}
		
	}
	
	//Méthode déssinant les entités
	public void draw(Graphics2D g) {
		// cycle round drawing all the entities we have in the game
		try
		{
	        for(Entity entity : entities.toArray(new Entity[entities.size()]))
				entity.draw(g);
		}
		catch(Exception e)
		{
			
		}
	}
	
	//Fonction permettant déplacer les entités
	public void moveEntities(long delta, LevelManager levelMan) {
		
		levelMan.getCurrentLevel().getCurrentStrategyMove().performMove(delta, this);
		
		for(Entity entity : entities.toArray(new Entity[entities.size()]))
		{
	    	if (entity instanceof EntityEnnemy)
	    		continue; // géré par la stratégie de déplacement
			entity.move(delta);
		}
	}
	
	public void makeEntitiesShoot(LevelManager levelMan) {
		levelMan.getCurrentLevel().getCurrentStrategyShot().performShot(this);
	}
	
	
	
	
	public void doCollisions() {
		
		// brute force collisions, compare every entity against
		// every other entity. If any of them collide notify 
		// both entities that the collision has occured
		
		entitiesMT = entities.toArray(new Entity[entities.size()]);
		
		threadpool = Executors.newFixedThreadPool(nbThread);

		for (int cTh=0; cTh<nbThread; cTh++)
		{
			threadsRunnable[cTh].collisionWork(entitiesMT);
			threadpool.submit(threadsRunnable[cTh]);
		}
		
		threadpool.shutdown();
		
		try {
			threadpool.awaitTermination(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		// remove any entity that has been marked for clear up
		entities.removeAll(removeList);
		removeList.clear();
	}
	
	
	public List<Entity> getEntitiesList() { return entities; }
	
	
	public int getTotalRemainingEnnemyLife()
	{
		int s = 0;
		for (Entity e : entities.toArray(new Entity[entities.size()]))
		{
			if (e instanceof EntityEnnemy)
				s+= e.getLife();
		}
		return s;
	}
	
	
	/**
	 * Remove an entity from the game. The entity removed will
	 * no longer move or be drawn.
	 * 
	 * @param entity The entity that should be removed
	 */
	public synchronized void removeEntity(Entity entity) {
		entity.plannedToRemoved = true;
		removeList.add(entity);
	}
	
	
	
	
	List<Entity> getRemovedList() { return removeList; }

	public int getLastCollisionComputingNumber() { return lastCollisionComputingNumber; }
	
	
	
	
	
	
	
}



