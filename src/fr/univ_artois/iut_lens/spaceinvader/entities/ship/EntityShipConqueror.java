package fr.univ_artois.iut_lens.spaceinvader.entities.ship;

import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShotFromAllySearch;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShipConqueror extends EntityShip {
	
	private int nbrShotPerInterval;

	public EntityShipConqueror(EntitiesManager eM, int fI, int nbS) {
		super("sprites/Faction9-Spaceships-by-MillionthVector_bluecarrier.png", 500, eM, fI);
		nbrShotPerInterval = nbS;
	}

	@Override
	public void shoot(long currentTime) {
		
		
		double pos_x0 = position.x+getBoundingBox().width/2-5*nbrShotPerInterval;
		for (int i=0; i<nbrShotPerInterval; i++)
		{
			entitiesManager.getEntitiesList().add(new EntityShotFromAllySearch(new Vector2d(pos_x0 + 10*i, position.y), new Vector2d(0, -100), entitiesManager));
		}
		
	}

}
