package fr.univ_artois.iut_lens.spaceinvader.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.util.Position;

public class EntityShotFromAllyAdvanced extends EntityShotFromAlly {

	public EntityShotFromAllyAdvanced(double x, double y, Position s,
			EntitiesManager eM) {
		super("sprites/shot2.gif", x, y, 2, s, eM);
		
	}

}
