package fr.univ_artois.iut_lens.spaceinvader.levels;

import java.util.ArrayList;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.strategy.move.StrategyMoveEnnemyNormal;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.strategy.move.StrategyMoveEnnemyRandom;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.strategy.shot.StrategyShotEnnemyAimFor;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.strategy.shot.StrategyShotEnnemyBasic;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

/**
 * Class which made appear the terrifying Marc and Maxime !!
 *
 */
public class LevelMegaAllStars extends Level {

	public LevelMegaAllStars(EntitiesManager entitiesManager) {
		super(entitiesManager,
				7,
				12,
				null,
				new Vector2d(50, 50),
				new Vector2d(50, 50),
				new StrategyMoveEnnemyNormal(),
				new StrategyShotEnnemyBasic(1000));
	}

	@Override
	public ArrayList<Entity> generateLevel() {
		nbCount = 0;
		SquadList = new ArrayList<Entity>();
		for (int r=0;r<row;r++) {
			for (int l=0;l<line;l++) {
				if (nbCount%2 == 0)
					SquadList.add(new EntityEnnemy("sprites/marc.jpg",new Vector2d(pos.x+(l*space.x),pos.y+r*space.y), 100, entitiesManager));
				else
					SquadList.add(new EntityEnnemy("sprites/max.jpg",new Vector2d(pos.x+(l*space.x),pos.y+r*space.y), 100, entitiesManager));
				
				nbCount++;
			}
		}
		return SquadList;
	}
	
	public boolean hasOneDestroyed() {
		
		boolean ret = super.hasOneDestroyed();
		
		if (nbCount == 60)
		{
			strategyMove = new StrategyMoveEnnemyRandom();
			strategyShot = new StrategyShotEnnemyAimFor(5);
		}
		
		return ret;
		
	}
}
