package fr.univ_artois.iut_lens.spaceinvader.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.shot.EntityShotFromAlly;
import fr.univ_artois.iut_lens.spaceinvader.util.Rectangle;
/**
 * Cette classe sert juste à gérer les entitées
 *
 */
public class EntitiesManager {

	public final CollisionTree entities = new CollisionTree(new Rectangle(-500, -500, MegaSpaceInvader.DISPLAY_WIDTH + 1000, MegaSpaceInvader.DISPLAY_HEIGHT + 1000));
	
	private final List<Entity> addList = new ArrayList<>();
	private final List<Entity> removeList = new ArrayList<>();
	
	private boolean canDirectlyAdd = true;
	private List<Entity> listSnapshot = null;
	
	// ces deux listes servent pour l'optimisation du réseau
	private List<Integer> idsAddedEntity = new ArrayList<>();
	private List<Integer> idsRemovedEntity = new ArrayList<>();
	
	
	
	
	
	//Fonction permettant de déplacer les entités et de calculer leur collisions
	public void moveAndCollideEntities(long delta, LevelManager levelMan) {
		
		
		levelMan.getCurrentLevel().getCurrentStrategyMove().performMove(delta, this);
		
		synchronized (this) {
			canDirectlyAdd = false;
			listSnapshot = new ArrayList<>(size());
			entities.forEach(listSnapshot::add);
		}
		
		entities.parallelStream().forEach(e -> {
			if (!(e instanceof EntityEnnemy))
				e.move(delta);
		});
		
		entities.updatePositions();
		
		entities.computeCollision(true);
		
		synchronized (this) {
			entities.removeAll(removeList);
			removeList.clear();
			entities.addAll(addList);
			addList.clear();
			canDirectlyAdd = true;
			listSnapshot = null;
		}
		
	}
	
	public void makeEntitiesShoot(LevelManager levelMan) {
		levelMan.getCurrentLevel().getCurrentStrategyShot().performShot(this);
	}
	
	
	public synchronized boolean add(Entity e) {
		int size = size();
		if (e instanceof EntityShotFromAlly && size > MegaSpaceInvader.SERVER_NB_MAX_ENTITY)
			if (MegaSpaceInvader.RANDOM.nextInt(size - MegaSpaceInvader.SERVER_NB_MAX_ENTITY) > 1)
				return false;
		if (canDirectlyAdd)
			entities.add(e);
		else
			addList.add(e);
		idsAddedEntity.add(e.id);
		return true;
	}
	
	
	public synchronized void addAll(Collection<? extends Entity> ents) {
		ents.forEach(this::add);
	}
	
	
	public synchronized int size() {
		return entities.size() + addList.size();
	}
	
	public synchronized void clear() {
		entities.clear();
		idsAddedEntity.clear();
		idsRemovedEntity.clear();
	}
	
	
	public synchronized int getTotalRemainingEnnemyLife()
	{
		AtomicInteger s = new AtomicInteger(0);
		entities.forEach(e -> {
			if (e instanceof EntityEnnemy)
				s.addAndGet(e.getLife());
		});
		return s.intValue();
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

	
	public synchronized List<Entity> getEntityListSnapshot() {
		if (canDirectlyAdd) {
			listSnapshot = new ArrayList<>(size());
			entities.forEach(listSnapshot::add);
		}
		
		return new ArrayList<>(listSnapshot);
	}
	
	public synchronized List<Integer> getRemovedEntities() {
		List<Integer> list = idsRemovedEntity;
		idsRemovedEntity = new ArrayList<>();
		return list;
	}
	
	public synchronized List<Integer> getAddedEntities() {
		List<Integer> list = idsAddedEntity;
		idsAddedEntity = new ArrayList<>();
		return list;
	}
	
	
	
	
	
	
}



