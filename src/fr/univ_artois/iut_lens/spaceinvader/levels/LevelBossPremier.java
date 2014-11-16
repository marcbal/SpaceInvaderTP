package fr.univ_artois.iut_lens.spaceinvader.levels;

import java.util.ArrayList;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.strategy.move.StrategyMoveEnnemyBossPremier;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.strategy.shot.StrategyShotEnnemyBasic;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

/**
 * Class which made appear the horrible explosive Maxime !!
 *
 */
public class LevelBossPremier extends Level {

	public LevelBossPremier(EntitiesManager entitiesManager) {
		super(entitiesManager,
				5,
				10,
				"sprites/alien_spaceship_by_animot-d5t4j61.png",
				new Vector2d(50, 50),
				new Vector2d(100, 50),
				new StrategyMoveEnnemyBossPremier(),
				new StrategyShotEnnemyBasic(100));
		
	}
	
	@Override
	public ArrayList<Entity> generateLevel() {
		nbCount = 0;
		SquadList = new ArrayList<Entity>();
		Entity alien = new EntityEnnemy(sprite,new Vector2d(400,0), 1000, entitiesManager);
		SquadList.add(alien);
		nbCount++;
		return SquadList;
	}
}
