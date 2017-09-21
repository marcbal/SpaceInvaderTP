package fr.univ_artois.iut_lens.spaceinvader.server;

import java.util.List;

import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;

public class CollisionThread implements Runnable {
	
	private Entity me;
	private List<Entity> others;
	
	public CollisionThread(Entity m, List<Entity> o) {
		me = m;
		others = o;
	}
	
	
	@Override
	public void run() {
		
		for (Entity him : others) {
			try
			{
				if (him.plannedToRemoved()) continue;
				
				if (him.collidesWith(me)) {
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
		
		
	}
	

}
