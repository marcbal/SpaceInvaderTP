package fr.univ_artois.iut_lens.spaceinvader.entities;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.util.Position;

public class EntityShotFromAllyBasic extends EntityShotFromAlly {

	public EntityShotFromAllyBasic(double x,
			double y, Position s, EntitiesManager eM) {
		super("sprites/shot.gif", x, y, 1, s, eM);
		
	}

}
