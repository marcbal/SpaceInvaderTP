package fr.univ_artois.iut_lens.spaceinvader.levels;

import java.util.ArrayList;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.strategy.move.StrategyMoveEnnemyDisturbed;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.strategy.move.StrategyMoveEnnemyFinalBoss;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.strategy.move.StrategyMoveEnnemyRandom;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.strategy.shot.StrategyShotEnnemyAimFor;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.strategy.shot.StrategyShotEnnemyMegaBoss;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

/**
 * Class which made appear the horrible explosive Maxime !!
 *
 */
public class LevelFinalBoss extends Level {
	
	public LevelFinalBoss(EntitiesManager entitiesManager) {
		super(entitiesManager,
				1,
				1,
				"sprites/spartanm1.png",
				new Vector2d(50, 50),
				new Vector2d(100, 50),
				new StrategyMoveEnnemyRandom(),
				new StrategyShotEnnemyMegaBoss());
		
	}
	
	@Override
	public ArrayList<Entity> generateLevel() {
		nbCount = 0;
		SquadList = new ArrayList<Entity>();
		Entity alien = new EntityEnnemy(sprite,new Vector2d(250,0), 999999, entitiesManager);
		SquadList.add(alien);
		Entity alien2 = new EntityEnnemy("sprites/max.jpg",new Vector2d(50,100), 7500, entitiesManager);
		SquadList.add(alien2);
		Entity alien3 = new EntityEnnemy("sprites/marc.jpg",new Vector2d(650,100),7500, entitiesManager);
		SquadList.add(alien3);
		nbCount = 3;
		return SquadList;
	}
	
	public boolean hasOneDestroyed() {
		
		boolean ret = super.hasOneDestroyed();
		
		if(nbCount==2) {
			strategyMove = new StrategyMoveEnnemyDisturbed();
			strategyShot = new StrategyShotEnnemyAimFor(10);
		}
		
		if(nbCount==1) {
			strategyMove = new StrategyMoveEnnemyFinalBoss();
			strategyShot = new StrategyShotEnnemyAimFor(2);
		}
		return ret;
		
	}
}
