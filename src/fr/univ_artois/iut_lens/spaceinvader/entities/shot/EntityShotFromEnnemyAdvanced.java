package fr.univ_artois.iut_lens.spaceinvader.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.CircleEntity;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromEnnemyAdvanced extends EntityShotFromEnnemy implements CircleEntity {

	public EntityShotFromEnnemyAdvanced(Vector2d p, Vector2d s, EntitiesManager eM) {
		super("sprites/shot2_red.png", p, 2, 5, s, eM);
	}

}
