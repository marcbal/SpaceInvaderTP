package fr.univ_artois.iut_lens.spaceinvader;

import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;

public class CollisionThread implements Runnable {
	
	
	private int itemIndex;
	private Entity[] entitiesMT = null;
	
	public CollisionThread(int index, Entity[] ents) {
		if (ents == null)
			throw new IllegalArgumentException("ents ne peut être null");
		if (index < 0 || index >= ents.length)
			throw new IllegalArgumentException("index n'est pas entre 0 et la taille de ents");
		itemIndex = index;
		entitiesMT = ents;
	}
	
	
	@Override
	public void run() {
		Entity me = entitiesMT[itemIndex];
		if (me.plannedToRemoved())
			return;
		
		for (int i=0; i<itemIndex; i++) {
			Entity him = entitiesMT[i];
			try
			{
				if (him.getCamp().equals(me.getCamp())) continue;
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
