package fr.univ_artois.iut_lens.spaceinvader.server.levels;

import java.util.ArrayList;
import java.util.List;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.move.StrategyMoveEnnemyNormal;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.move.StrategyMoveEnnemyRandom;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.shot.StrategyShotEnnemyAimFor;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.shot.StrategyShotEnnemyBasic;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

/**
 * Class which made appear the terrifying Marc !!
 *
 */
public class LevelMarc extends Level {

	public LevelMarc(EntitiesManager entitiesManager) {
		super(entitiesManager,
				7,
				10,
				"sprites/marc.jpg",
				new Vector2d(50, 50),
				new Vector2d(50, 50),
				new StrategyMoveEnnemyNormal(),
				new StrategyShotEnnemyBasic(1000));
	}

	@Override
	public List<EntityEnnemy> generateLevel() {
		SquadList = new ArrayList<>();
		for (int r=0;r<row;r++) {
			for (int l=0;l<line;l++) {
				EntityEnnemy alien = new EntityEnnemy(sprite,new Vector2d(pos.x+(l*space.x),pos.y+r*space.y), 5,entitiesManager);
				SquadList.add(alien);
			}
		}
		return SquadList;
	}
	
	@Override
	public boolean hasOneEnnemyDestroyed() {
		
		boolean ret = super.hasOneEnnemyDestroyed();
		
		if (getCount() == 40)
		{
			strategyMove = new StrategyMoveEnnemyRandom();
			strategyShot = new StrategyShotEnnemyAimFor(600);
		}
		
		return ret;
		
	}
}
