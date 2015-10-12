package fr.univ_artois.iut_lens.spaceinvader.server.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ship.EntityShip;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllyAdvanced extends EntityShotFromAlly {

	public EntityShotFromAllyAdvanced(Vector2d p, Vector2d s,
			EntitiesManager eM, EntityShip ship) {
		super("sprites/shot2_blue.png", p, 2, 2, s, eM, ship);
		
	}

}
