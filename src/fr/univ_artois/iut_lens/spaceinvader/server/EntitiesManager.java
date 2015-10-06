package fr.univ_artois.iut_lens.spaceinvader.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.list.TreeList;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ship.EntityShip;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ship.ShipLimitedShot;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.shot.EntityShot;
/**
 * Cette classe sert juste à gérer les entitées
 *
 */
public class EntitiesManager {

	private final List<Entity> entities = new TreeList<Entity>();
	private final List<Entity> removeList = new ArrayList<Entity>();
	
	
	
	
	//Fonction permettant de déplacer les entités et de calculer leur collisions
	public void moveAndCollideEntities(long delta, LevelManager levelMan) {
		
		levelMan.getCurrentLevel().getCurrentStrategyMove().performMove(delta, this);
		
		ExecutorService threadpool = Executors.newFixedThreadPool(MegaSpaceInvader.SERVER_NB_THREAD_FOR_ENTITY_COLLISION);

		// partie de calcul multithreadé
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
		synchronized (this) {
			entities.add(shot);
		}
		return true;
	}
	
	
	public synchronized int getTotalRemainingEnnemyLife()
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
	public void removeEntity(Entity entity) {
		entity.planToRemove();
		synchronized (this) {
			removeList.add(entity);
		}
	}
	
	
	
	
	public synchronized List<Entity> getRemovedList() { return removeList; }

	
	public synchronized Entity[] getEntityListSnapshot() {
		return entities.toArray(new Entity[entities.size()]);
	}
	
	
	
	
	
	
}



