package fr.univ_artois.iut_lens.spaceinvader;

import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShotFromAlly;
import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShotFromEnnemy;

public class CollisionThread implements Runnable {
	
	
	private int currentThread;
	private int nbThread;
	private Entity[] entitiesMT = null;
	
	public CollisionThread(int cTh, int nbTh) {
		currentThread = cTh;
		nbThread = nbTh;
	}
	
	
	@Override
	public void run() {
		if (entitiesMT == null) return;
		
		int s=currentThread;
		
		for (int p=0;p<entitiesMT.length;p++) {
			for (;s<entitiesMT.length;s+=nbThread) {
				try
				{
					Entity me = entitiesMT[p];
					Entity him = entitiesMT[s];
					if (me instanceof EntityShotFromAlly && him instanceof EntityShotFromAlly) continue;
					if (me instanceof EntityShotFromEnnemy && him instanceof EntityShotFromEnnemy) continue;
					if (me instanceof EntityEnnemy && him instanceof EntityEnnemy) continue;
					if (me.plannedToRemoved || him.plannedToRemoved) continue;
					
					if (me.collidesWith(him)) {
						me.collidedWith(him);
						him.collidedWith(me);
					}
				}
				catch (Exception e)
				{
					/*System.err.println("Thread #"+cTh
							+", Collision #"+i
							+", entities.size()="+entities.size()
							+", compare "+p+" and "+s+" indexes :");
					e.printStackTrace();*/
				}
			}
			s+=p+2-entitiesMT.length;
		}
		
		
		
		
		entitiesMT = null;
	}
	
	public void collisionWork(Entity[] ent) {
		entitiesMT = ent;
	}

}
