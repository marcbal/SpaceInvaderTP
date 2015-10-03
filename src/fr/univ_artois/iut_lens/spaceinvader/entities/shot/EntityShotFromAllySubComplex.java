package fr.univ_artois.iut_lens.spaceinvader.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllySubComplex extends EntityShotFromAlly {

	public EntityShotFromAllySubComplex(Vector2d p, Vector2d s,
			EntitiesManager eM) {
		super("sprites/UnderComplexShot.png", p, 10, 2, s, eM);
		
	}

}
