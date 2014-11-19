package fr.univ_artois.iut_lens.spaceinvader.entities.ship;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShotFromAllySearch;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShipConqueror extends EntityShip {
	protected long lastFireTime = 0; //Dernier tir du vaisseau
	private long fireInterval = 100; //Intervalle de temps par défaut pour lequel le vaisseau peut tirer
	private int nbrShotPerInterval = 1;

	public EntityShipConqueror(EntitiesManager eM, int fI, int nbS) {
		super("sprites/Faction9-Spaceships-by-MillionthVector_bluecarrier.png", 500, eM);
		fireInterval = fI;
		nbrShotPerInterval = nbS;
	}

	@Override
	public void tryToShoot(long currentTime) {
		if(currentTime - lastFireTime < fireInterval) return; //L'interval de tir est trop court
		lastFireTime = currentTime; //On met le dernier tire au temps actuel
		
		
		double pos_x0 = position.x+getBoundingBox().width/2-5*nbrShotPerInterval;
		for (int i=0; i<nbrShotPerInterval; i++)
		{
			entitiesManager.getEntitiesList().add(new EntityShotFromAllySearch(new Vector2d(pos_x0 + 10*i, position.y), new Vector2d(0, -100), entitiesManager));
		}
		
	}

}
