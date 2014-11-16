package fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.strategy.shot;

import java.util.Random;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.Game;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.entities.ship.EntityShip;
import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShotFromEnnemyAdvanced;
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
		EntityShip ship = Game.gameInstance.getShipManager().getCurrentShip();
		Random r = new Random();
		// on doit parcourir une copie de l'entityList,
		// car on rajoute des entité dans l'original, à l'intérieur de cette boucle
		for(Entity entity : entMan.getEntitiesList().toArray(new Entity[1])) {
			if (entity instanceof EntityEnnemy)
			{
				if (r.nextInt(scarcity)==0)
				{
					Vector2d speed = ship.getPosition().dup().add(entity.getPosition().dup().invert()).dotProduct(0.4);
					entMan.getEntitiesList().add(new EntityShotFromEnnemyAdvanced(new Vector2d(entity.getPosition().x+entity.getBoundingBox().width/2, entity.getPosition().y+entity.getBoundingBox().height), speed, entMan));
				}
					
			}
		}
	}

}
