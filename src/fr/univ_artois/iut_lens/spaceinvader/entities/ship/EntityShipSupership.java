package fr.univ_artois.iut_lens.spaceinvader.entities.ship;

import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShotFromAllyAdvanced;
import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShotFromAllyBasic;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShipSupership extends EntityShip {
	
	public EntityShipSupership(EntitiesManager eM) {
		super("sprites/supership1.png", 400, eM, 50);
	}

	@Override
	public void shoot(long currentTime) {
		
		entitiesManager.getEntitiesList().add(new EntityShotFromAllyAdvanced(new Vector2d(position.x+getBoundingBox().width/2-20, position.y), new Vector2d(0, -300), entitiesManager));
		entitiesManager.getEntitiesList().add(new EntityShotFromAllyBasic(new Vector2d(position.x+getBoundingBox().width/2-10, position.y), new Vector2d(-150, -1000), entitiesManager));
		entitiesManager.getEntitiesList().add(new EntityShotFromAllyBasic(new Vector2d(position.x+getBoundingBox().width/2, position.y), new Vector2d(150, -1000), entitiesManager));
		entitiesManager.getEntitiesList().add(new EntityShotFromAllyAdvanced(new Vector2d(position.x+getBoundingBox().width/2+10, position.y), new Vector2d(0, -300), entitiesManager));

	}

}
