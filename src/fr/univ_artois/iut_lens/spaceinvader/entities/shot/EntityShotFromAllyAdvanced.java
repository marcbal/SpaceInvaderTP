package fr.univ_artois.iut_lens.spaceinvader.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.CircleEntity;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllyAdvanced extends EntityShotFromAlly implements CircleEntity {

	public EntityShotFromAllyAdvanced(Vector2d p, Vector2d s,
			EntitiesManager eM) {
		super("sprites/shot2_blue.png", p, 2, 2, s, eM);
		
	}

}
