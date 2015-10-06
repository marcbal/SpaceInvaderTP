package fr.univ_artois.iut_lens.spaceinvader.server.levels;

import java.util.ArrayList;
import java.util.List;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.move.StrategyMoveEnnemyBossPremier;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.shot.StrategyShotEnnemyMegaBoss;
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
				new StrategyShotEnnemyMegaBoss());
		
	}
	
	@Override
	public List<EntityEnnemy> generateLevel() {
		SquadList = new ArrayList<EntityEnnemy>();
		EntityEnnemy alien = new EntityEnnemy(sprite,new Vector2d(400,0), 2500, entitiesManager);
		SquadList.add(alien);
		return SquadList;
	}
}
