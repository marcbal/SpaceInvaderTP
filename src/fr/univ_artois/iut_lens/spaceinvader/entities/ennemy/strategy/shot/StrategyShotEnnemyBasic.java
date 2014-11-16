package fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.strategy.shot;

import java.util.Random;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.*;
import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShotFromEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class StrategyShotEnnemyBasic extends StrategyShotEnemy {

	@Override
	public void performShot(EntitiesManager entMan) {
		Random r = new Random();
		for(Entity entity : entMan.getEntitiesList()) {
			System.out.println(entity.getPosition().x + " " + entity.getPosition().y);
		    entMan.getEntitiesList().add(new EntityShotFromEnnemy(new Vector2d(entity.getPosition().x+entity.getBoundingBox().width/2, entity.getPosition().y+entity.getBoundingBox().height), new Vector2d(0, 200), entMan));
		}
	}
}