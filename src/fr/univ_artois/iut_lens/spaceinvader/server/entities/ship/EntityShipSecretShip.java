package fr.univ_artois.iut_lens.spaceinvader.server.entities.ship;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.shot.EntityShotFromAllyComplex;
import fr.univ_artois.iut_lens.spaceinvader.server.players.ShipManager;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShipSecretShip extends EntityShip {
	
	public EntityShipSecretShip(EntitiesManager eM, ShipManager sm, long fireInter) {
		super("sprites/SecretShip.png", 800, eM, fireInter, sm);
	}

	@Override
	public void shoot(long currentTime) {
		
		entitiesManager.add(new EntityShotFromAllyComplex(new Vector2d(position.x+getBoundingBox().width/2-10, position.y), entitiesManager));
	}

}
