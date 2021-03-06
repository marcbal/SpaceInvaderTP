package fr.univ_artois.iut_lens.spaceinvader.server.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ship.EntityShip;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllySubComplex extends EntityShotFromAlly {

	public EntityShotFromAllySubComplex(Vector2d p, Vector2d s,
			EntitiesManager eM, EntityShip ship) {
		super("sprites/UnderComplexShot.png", p, 20, 2, s, eM, ship);
		
	}

}
