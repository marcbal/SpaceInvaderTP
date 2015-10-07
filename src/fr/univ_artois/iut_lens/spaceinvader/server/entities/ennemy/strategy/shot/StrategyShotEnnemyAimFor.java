package fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.shot;

import java.util.Random;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ship.EntityShip;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.shot.EntityShotFromEnnemyAdvanced;
import fr.univ_artois.iut_lens.spaceinvader.util.TargettingUtil;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class StrategyShotEnnemyAimFor extends StrategyShotEnnemy {
	
	/** rareté de tir (1 -> tout le temps, à chaque frame, 2 -> 2 fois moins de tir) */
	protected int scarcity;

	public StrategyShotEnnemyAimFor(int s) {
		if (s<=0) throw new IllegalArgumentException("scarcity must be > 0");
		
		scarcity = s;
	}

	@Override
	public void performShot(EntitiesManager entMan) {
		Random r = new Random();
		// on doit parcourir une copie de l'entityList,
		// car on rajoute des entité dans l'original, à l'intérieur de cette boucle
		for(Entity entity : entMan.getEntitiesList().toArray(new Entity[1])) {
			if (entity instanceof EntityEnnemy)
			{
				if (r.nextInt(scarcity)==0)
				{
					EntityShip ship = TargettingUtil.searchTargetEnnemy(entity, entMan, EntityShip.class, false);
					if (ship == null) continue;
					Vector2d speed = ship.getPosition().add(entity.getPosition().invert()).dotProduct(0.4);
					entMan.getEntitiesList().add(new EntityShotFromEnnemyAdvanced(new Vector2d(entity.getPosition().x+entity.getBoundingBox().width/2, entity.getPosition().y+entity.getBoundingBox().height), speed, entMan));
				}
					
			}
		}
	}

}
