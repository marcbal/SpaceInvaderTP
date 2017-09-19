package fr.univ_artois.iut_lens.spaceinvader.server.levels;

import java.util.ArrayList;
import java.util.List;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.move.StrategyMoveEnnemyFinalBoss;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.move.StrategyMoveEnnemyRandom;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.shot.StrategyShotEnnemyAimFor;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.shot.StrategyShotEnnemyMegaBoss;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class LevelFinalBoss2 extends Level {

	public LevelFinalBoss2(EntitiesManager entitiesManager) {
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
		SquadList = new ArrayList<>();
		SquadList.add(new EntityEnnemy(sprite,new Vector2d(10,10), 999999, entitiesManager));
		SquadList.add(new EntityEnnemy(sprite,new Vector2d(250,10), 999999, entitiesManager));
		SquadList.add(new EntityEnnemy(sprite,new Vector2d(510,10), 999999, entitiesManager));
		return SquadList;
	}
	
	@Override
	public boolean hasOneEnnemyDestroyed() {
		
		boolean ret = super.hasOneEnnemyDestroyed();
		
		if(getCount()==2) {
			strategyMove = new StrategyMoveEnnemyFinalBoss();
			strategyShot = new StrategyShotEnnemyAimFor(2);
		}
		return ret;
		
	}

}
