package fr.univ_artois.iut_lens.spaceinvader.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllyAdvanced extends EntityShotFromAlly {

	public EntityShotFromAllyAdvanced(Vector2d p, Vector2d s,
			EntitiesManager eM) {
		super("sprites/shot2_blue.gif", p, 2, s, eM);
		
	}

}
