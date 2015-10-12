package fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.shot;

import java.util.Random;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.*;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.shot.EntityShotFromEnnemyBasic;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class StrategyShotEnnemyBasic extends StrategyShotEnnemy {
	
	/** rareté de tir (1 -> tout le temps, à chaque frame, 2 -> 2 fois moins de tir) */
	private int scarcity;
	
	public StrategyShotEnnemyBasic(int s) {
		if (s<=0) throw new IllegalArgumentException("scarcity must be > 0");
		
		scarcity = s;
	}
	
	
	@Override
	public void performShot(EntitiesManager entMan) {
		Random r = MegaSpaceInvader.RANDOM;
		// on doit parcourir une copie de l'entityList,
		// car on rajoute des entité dans l'original, à l'intérieur de cette boucle
		for(Entity entity : entMan.getEntityListSnapshot()) {
			if (entity instanceof EntityEnnemy)
			{
				if (r.nextInt(scarcity)==0)
					entMan.add(new EntityShotFromEnnemyBasic(new Vector2d(entity.getPosition().x+entity.getBoundingBox().width/2, entity.getPosition().y+entity.getBoundingBox().height), new Vector2d(0, 200), entMan));
			}
		}
	}
}