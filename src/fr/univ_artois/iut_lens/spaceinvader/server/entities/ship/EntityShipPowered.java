package fr.univ_artois.iut_lens.spaceinvader.server.entities.ship;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.shot.EntityShotFromAllyBasic;
import fr.univ_artois.iut_lens.spaceinvader.server.players.ShipManager;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShipPowered extends EntityShip {
	
	public EntityShipPowered(EntitiesManager eM, ShipManager sm) {
		super("sprites/spaceship1.png", 300, eM, 100, sm);
	}

	@Override
	public void shoot(long currentTime) {
		
		entitiesManager.add(new EntityShotFromAllyBasic(new Vector2d(getPosition().x+getBoundingBox().width/2-10, getPosition().y), new Vector2d(-100, -1000), entitiesManager, this));
		entitiesManager.add(new EntityShotFromAllyBasic(new Vector2d(getPosition().x+getBoundingBox().width/2, getPosition().y), new Vector2d(100, -1000), entitiesManager, this));
		
	}

}
