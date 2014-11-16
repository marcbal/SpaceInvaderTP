package fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.strategy.shot;

import java.util.Random;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShotFromEnnemyAdvanced;
import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShotFromEnnemyBasic;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class StrategyShotEnnemyMegaBoss extends StrategyShotEnnemy {
	
	private long frameCount = 0;
	
	private double speedVerticalShot = 300;
	
	private int nbShotCircle = 20;
	
	private double circleSpeed = 150;

	@Override
	public void performShot(EntitiesManager entMan) {
		frameCount++;
		
		Random r = new Random();
		
		// tir du haut de l'écran
		if (frameCount%400==0)
		{
			double randSpeedLR = 100*r.nextDouble() - 50;
			for (int i=20; i<800; i+=20)
			{
				if (r.nextInt(4)<3)
					entMan.getEntitiesList().add(new EntityShotFromEnnemyBasic(new Vector2d(i, -10), new Vector2d(randSpeedLR, speedVerticalShot), entMan));
			}
		}
		
		// normalement, yen a qu'un seul, mais bon
		for(Entity entity : entMan.getEntitiesList().toArray(new Entity[1])) {
			if (entity instanceof EntityEnnemy && frameCount%50==0)
			{
				for (double i = 0; i<2*Math.PI; i+= (2*Math.PI)/(double)nbShotCircle)
					entMan.getEntitiesList().add(
							new EntityShotFromEnnemyAdvanced(
									new Vector2d(entity.getPosition().x+entity.getBoundingBox().width/2, entity.getPosition().y+entity.getBoundingBox().height),
									new Vector2d(Math.cos(i)*circleSpeed, Math.sin(i)*circleSpeed),
									entMan));
			}
		}
	}
}
