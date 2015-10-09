package fr.univ_artois.iut_lens.spaceinvader.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.collections4.list.TreeList;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;
/**
 * Cette classe sert juste à gérer les entitées
 *
 */
public class EntitiesManager {

	private ExecutorService threadpool = Executors.newFixedThreadPool(MegaSpaceInvader.SERVER_NB_THREAD_FOR_ENTITY_COLLISION);

	private final List<Entity> entities = new TreeList<Entity>();
	private final List<Entity> removeList = new ArrayList<Entity>();
	
	// ces deux listes servent pour l'optimisation du réseau
	private List<Integer> idsAddedEntity = new ArrayList<Integer>();
	private List<Integer> idsRemovedEntity = new ArrayList<Integer>();
	
	
	
	
	//Fonction permettant de déplacer les entités et de calculer leur collisions
	public void moveAndCollideEntities(long delta, LevelManager levelMan) {
		
		levelMan.getCurrentLevel().getCurrentStrategyMove().performMove(delta, this);
		
		List<Future<?>> pendingCollisionTasks = new ArrayList<Future<?>>();
		
		// partie de calcul multithreadé
		Entity[] entitiesArray;
		synchronized (this) {
			entitiesArray = entities.toArray(new Entity[entities.size()]);
		}
		
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
	    	
	    	pendingCollisionTasks.add(threadpool.submit(new CollisionThread(i, entitiesArray)));
		}

		for (Future<?> pendingTask : pendingCollisionTasks) {
			try {
				pendingTask.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		
		synchronized (this) {
			entities.removeAll(removeList);
			removeList.clear();
		}
		
	}
	
	public void makeEntitiesShoot(LevelManager levelMan) {
		levelMan.getCurrentLevel().getCurrentStrategyShot().performShot(this);
	}
	
	
	public synchronized void add(Entity e) {
		entities.add(e);
		idsAddedEntity.add(e.id);
	}
	
	
	public synchronized void addAll(Collection<? extends Entity> ents) {
		entities.addAll(ents);
		for (Entity e : ents) {
			idsAddedEntity.add(e.id);
		}
	}
	
	
	public synchronized int size() {
		return entities.size();
	}
	
	public synchronized void clear() {
		entities.clear();
		idsAddedEntity.clear();
		idsRemovedEntity.clear();
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
	public synchronized void remove(Entity entity) {
		entity.planToRemove();
		synchronized (this) {
			removeList.add(entity);
			idsRemovedEntity.add(entity.id);
		}
	}
	
	
	
	
	public synchronized List<Entity> getRemovedList() { return removeList; }

	
	public synchronized Entity[] getEntityListSnapshot() {
		return entities.toArray(new Entity[entities.size()]);
	}
	
	public synchronized List<Integer> getRemovedEntities() {
		List<Integer> list = idsRemovedEntity;
		idsRemovedEntity = new ArrayList<Integer>();
		return list;
	}
	
	public synchronized List<Integer> getAddedEntities() {
		List<Integer> list = idsAddedEntity;
		idsAddedEntity = new ArrayList<Integer>();
		return list;
	}
	
	
	
	
	
	
}



