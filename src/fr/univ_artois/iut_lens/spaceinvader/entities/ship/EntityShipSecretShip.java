package fr.univ_artois.iut_lens.spaceinvader.entities.ship;

import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShotFromAllyComplex;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShipSecretShip extends EntityShip {
	
	public EntityShipSecretShip(EntitiesManager eM, long fireInter) {
		super("sprites/SecretShip.png", 800, eM, fireInter);
	}

	@Override
	public void shoot(long currentTime) {
		
		entitiesManager.getEntitiesList().add(new EntityShotFromAllyComplex(new Vector2d(position.x+getBoundingBox().width/2-10, position.y), entitiesManager));
	}

}
