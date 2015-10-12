package fr.univ_artois.iut_lens.spaceinvader.server.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ship.EntityShip;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllyBasic extends EntityShotFromAlly {

	public EntityShotFromAllyBasic(Vector2d p, Vector2d s, EntitiesManager eM, EntityShip ship) {
		super("sprites/shot_blue.png", p, 1, 1, s, eM, ship);
		
	}

}
