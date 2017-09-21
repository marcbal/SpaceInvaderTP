package fr.univ_artois.iut_lens.spaceinvader.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.list.TreeList;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity.Camp;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.shot.EntityShotFromAlly;
import fr.univ_artois.iut_lens.spaceinvader.util.Logger;
/**
 * Cette classe sert juste à gérer les entitées
 *
 */
public class EntitiesManager {

	private final Map<Camp, List<Entity>> entities = new EnumMap<>(Camp.class);
	
	private final List<Entity> removeList = new ArrayList<>();
	
	// ces deux listes servent pour l'optimisation du réseau
	private List<Integer> idsAddedEntity = new ArrayList<>();
	private List<Integer> idsRemovedEntity = new ArrayList<>();
	
	
	
	public EntitiesManager() {
		for (Camp c : Camp.values()) {
			entities.put(c, new TreeList<>());
		}
	}
	
	
	//Fonction permettant de déplacer les entités et de calculer leur collisions
	public void moveAndCollideEntities(long delta, LevelManager levelMan) {
		
		levelMan.getCurrentLevel().getCurrentStrategyMove().performMove(delta, this);
		
		
		/* Pour gagner du temps de calcul de collision, nous utilisons la
		 * stratégie suivante :
		 * Chaque entité appartient à un camp (enum Camp). Les entités
		 * appartenants au même camp ne rentrent pas en collision.
		 * De ce fait, ça ne sers à rien de confronter entre eux
		 * tous les entités d'un même camp. L'ancienne solution a été de
		 * mettre une condition "if(...) continue;" avant de tester la
		 * collision pour gagner du temps de calcul, mais ceci ne retire
		 * pas le temps de parcours de la liste (boucle, itérateur, ...).
		 * La séparation de chaque camps dans des listes distinctes permet
		 * de retirer le temps de boucle qui passeraient par des couples
		 * d'entités du même camps (une entitée d'une liste est toujours
		 * testée avec une entités d'une autre liste)
		 * 
		 *  Exemple : 3000 entités, 2900 alliés, 10 neutre, 90 énemies
		 *  Ancienne solution :
		 *  	9 000 000 tours de boucle pour 290 900 pairs d'entités
		 *  	hétérogènes
		 *  Nouvelle solution :
		 *  	2 900 * 10 + 2 900 * 90 + 10 * 90 = 290 900 tours de
		 *  	boucle, uniquement sur des pairs d'entités hétérogènes
		 *  	(Camp différent)
		 *  Dans ce cas de figure, le temps de calcul (uniquement le temps de bouclage
		 *  et itération, et hors calcul de collision et autres tests) serait
		 *  réduit de 96.8%
		 */
		List<Entity>[] lists;
		synchronized (this) {
			lists = entities.values().toArray(new List[entities.size()]);
			
			// use clone of lists instead of original that can be modified during iteration
			for (int i = 0; i < lists.length; i++) {
				lists[i] = new ArrayList<>(lists[i]);
			}
		}
		
		Arrays.sort(lists, (l1, l2) -> Integer.compare(l2.size(), l1.size()));
		
		
		for (int i = 0; i < lists.length; i++)
		{
			int iF = i;
			lists[i].parallelStream()
					.filter(el1 -> {
						try {
					    	if (!(el1 instanceof EntityEnnemy))
					    		// les enemies sont gérés par la stratégie de mouvement du level courant
					    		el1.move(delta);
					    	
					    	// some move() method's implementation want to remove the entity,
					    	// so we check after the move() method call
						} catch(Exception e) {
							Logger.severe(e.getMessage());
							e.printStackTrace();
						}
						return !el1.plannedToRemoved();
					})
					.forEach(el1 -> {
						for (int j = iF + 1; j < lists.length; j++) {
							for (Entity him : lists[j]) {
								try
								{
									if (him.plannedToRemoved()) continue;
									
									if (him.collidesWith(el1)) {
										el1.collidedWith(him);
										him.collidedWith(el1);
									}
								}
								catch (Exception e)
								{
									Logger.severe(e.getMessage());
									e.printStackTrace();
								}
							}
						}
					});
			
		}
		
		synchronized (this) {
			entities.values().forEach(l -> l.removeAll(removeList));
			removeList.clear();
		}
		
	}
	
	public void makeEntitiesShoot(LevelManager levelMan) {
		levelMan.getCurrentLevel().getCurrentStrategyShot().performShot(this);
	}
	
	
	public synchronized boolean add(Entity e) {
		if (e instanceof EntityShotFromAlly && size() > MegaSpaceInvader.SERVER_NB_MAX_ENTITY)
			if (MegaSpaceInvader.RANDOM.nextInt(500) > 1)
				return false;
		entities.get(e.getCamp()).add(e);
		idsAddedEntity.add(e.id);
		return true;
	}
	
	
	public synchronized void addAll(Collection<? extends Entity> ents) {
		ents.forEach(this::add);
	}
	
	
	public synchronized int size() {
		int c = 0;
		for (List<Entity> l : entities.values())
			c += l.size();
		return c;
	}
	
	public synchronized void clear() {
		for (List<Entity> l : entities.values())
			l.clear();
		idsAddedEntity.clear();
		idsRemovedEntity.clear();
	}
	
	
	public synchronized int getTotalRemainingEnnemyLife()
	{
		int s = 0;
		for (Entity e : entities.get(Camp.ENEMY))
		{
			if (e instanceof EntityEnnemy)
				s += e.getLife();
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

	
	public synchronized List<Entity> getEntityListSnapshot() {
		List<Entity> list = new ArrayList<>(size());
		entities.values().forEach(list::addAll);
		return list;
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



