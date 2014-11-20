package fr.univ_artois.iut_lens.spaceinvader.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromEnnemyBasic extends EntityShotFromEnnemy {

	public EntityShotFromEnnemyBasic(Vector2d p, Vector2d s, EntitiesManager eM) {
		super("sprites/shot_red.png", p, 1, 1, s, eM);
	}

}
