package fr.univ_artois.iut_lens.spaceinvader.entities.ship;

import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShotFromAllyBasic;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShipDefault extends EntityShip {
	
	public EntityShipDefault(EntitiesManager eM) {
		super("sprites/defaultship.png", 200, eM, 200);
	}

	@Override
	public void shoot(long currentTime) {
		entitiesManager.getEntitiesList().add(new EntityShotFromAllyBasic(new Vector2d(position.x+getBoundingBox().width/2-5, position.y), new Vector2d(0, -500), entitiesManager));
	}

}
