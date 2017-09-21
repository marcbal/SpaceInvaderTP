package fr.univ_artois.iut_lens.spaceinvader.server.entities.ship;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.shot.EntityShotFromAllyBasic;
import fr.univ_artois.iut_lens.spaceinvader.server.players.ShipManager;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShipDefault extends EntityShip {
	
	public EntityShipDefault(EntitiesManager eM, ShipManager sm) {
		super("sprites/defaultship.png", 200, eM, 200, sm);
	}

	@Override
	public void shoot(long currentTime) {
		entitiesManager.add(new EntityShotFromAllyBasic(new Vector2d(getPosition().x+getBoundingBox().width/2-5, getPosition().y), new Vector2d(0, -500), entitiesManager, this));
	}

}
