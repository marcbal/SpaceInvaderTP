package fr.univ_artois.iut_lens.spaceinvader.server.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllySubComplex2 extends EntityShotFromAlly {

	public EntityShotFromAllySubComplex2(Vector2d p, Vector2d s,
			EntitiesManager eM) {
		super("sprites/UnderComplexShot2.png", p, 20, 2, s, eM);
		
	}

}
