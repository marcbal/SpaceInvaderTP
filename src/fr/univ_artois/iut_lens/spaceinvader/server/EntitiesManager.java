package fr.univ_artois.iut_lens.spaceinvader.server;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import fr.univ_artois.iut_lens.spaceinvader.Game;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.entities.ship.EntityShip;
import fr.univ_artois.iut_lens.spaceinvader.entities.ship.ShipLimitedShot;
import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShot;
/**
 * Cette classe sert juste à gérer les entitées
 *
 */
public class EntitiesManager {

	private List<Entity> entities = new ArrayList<Entity>();
	private List<Entity> removeList = new ArrayList<Entity>();
	
	public final int nbThread = Runtime.getRuntime().availableProcessors();
	
	
	
	public EntitiesManager() {
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
	
	//Fonction permettant de déplacer les entités et de calculer leur collisions
	public void moveAndCollideEntities(long delta, LevelManager levelMan) {
		
		levelMan.getCurrentLevel().getCurrentStrategyMove().performMove(delta, this);
		
		ExecutorService threadpool = Executors.newFixedThreadPool(nbThread);

		// partie de calcul multithreadé
		long thread_colision_start = System.nanoTime();
		Entity[] entitiesArray = entities.toArray(new Entity[entities.size()]);
		for(int i = 0; i < entitiesArray.length; i++)
		{
			try {
				Entity entity = entitiesArray[i];
		    	if (!(entity instanceof EntityEnnemy))
		    		// les enemies sont gérés par la strtégie de mouvement ci-dessus
		    		entity.move(delta);
		    	
				if (entity.plannedToRemoved())
					continue;
			} catch(Exception e) {
				e.printStackTrace();
			}
	    	
	    	threadpool.submit(new CollisionThread(i, entitiesArray));
		}

		threadpool.shutdown();
		
		try {
			threadpool.awaitTermination(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadpool.shutdownNow();
		
		Game.gameInstance.logicalCollisionDuration.set(System.nanoTime()-thread_colision_start);
		
		entities.removeAll(removeList);
		removeList.clear();
	}
	
	public void makeEntitiesShoot(LevelManager levelMan) {
		levelMan.getCurrentLevel().getCurrentStrategyShot().performShot(this);
	}
	
	
	
	
	public List<Entity> getEntitiesList() { return entities; }
	
	
	
	public boolean addShotAlly(EntityShip ship, EntityShot shot) {
		if (ship instanceof ShipLimitedShot) {
			ShipLimitedShot limitedShip = (ShipLimitedShot) ship;
			if (limitedShip.getNbShotAlive() >= limitedShip.getMaxNbShot())
				return false;
		}
		entities.add(shot);
		return true;
	}
	
	
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
		entity.planToRemove();
		removeList.add(entity);
	}
	
	
	
	
	List<Entity> getRemovedList() { return removeList; }

	
	
	
	
	
	
	
}



