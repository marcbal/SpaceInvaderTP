package fr.univ_artois.iut_lens.spaceinvader.server.levels;

import java.util.ArrayList;
import java.util.List;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.move.StrategyMoveEnnemyDisturbed;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.move.StrategyMoveEnnemyFinalBoss;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.move.StrategyMoveEnnemyRandom;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.shot.StrategyShotEnnemyAimFor;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.shot.StrategyShotEnnemyMegaBoss;
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
	public List<EntityEnnemy> generateLevel() {
		SquadList = new ArrayList<EntityEnnemy>();
		EntityEnnemy alien = new EntityEnnemy(sprite,new Vector2d(250,0), 999999, entitiesManager);
		SquadList.add(alien);
		EntityEnnemy alien2 = new EntityEnnemy("sprites/max.jpg",new Vector2d(50,100), 20000, entitiesManager);
		SquadList.add(alien2);
		EntityEnnemy alien3 = new EntityEnnemy("sprites/marc.jpg",new Vector2d(650,100),20000, entitiesManager);
		SquadList.add(alien3);
		return SquadList;
	}
	
	public boolean hasOneEnnemyDestroyed() {
		
		boolean ret = super.hasOneEnnemyDestroyed();
		
		if(getCount()==2) {
			strategyMove = new StrategyMoveEnnemyDisturbed();
			strategyShot = new StrategyShotEnnemyAimFor(10);
		}
		
		if(getCount()==1) {
			strategyMove = new StrategyMoveEnnemyFinalBoss();
			strategyShot = new StrategyShotEnnemyAimFor(2);
		}
		return ret;
		
	}
}
