package fr.univ_artois.iut_lens.spaceinvader.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllyBasic extends EntityShotFromAlly {

	public EntityShotFromAllyBasic(Vector2d p, Vector2d s, EntitiesManager eM) {
		super("sprites/shot_blue.gif", p, 1, s, eM);
		
	}

}