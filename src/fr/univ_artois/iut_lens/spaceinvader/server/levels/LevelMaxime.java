package fr.univ_artois.iut_lens.spaceinvader.server.levels;

import java.util.ArrayList;
import java.util.List;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.move.StrategyMoveEnnemyDisturbed;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.shot.StrategyShotEnnemyBasic;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

/**
 * Class which made appear the horrible explosive Maxime !!
 *
 */
public class LevelMaxime extends Level {

	public LevelMaxime(EntitiesManager entitiesManager) {
		super(entitiesManager,
				5,
				10,
				"sprites/max.jpg",
				new Vector2d(50, 50),
				new Vector2d(100, 50),
				new StrategyMoveEnnemyDisturbed(),
				new StrategyShotEnnemyBasic(1500));
		
	}
	
	@Override
	public List<EntityEnnemy> generateLevel() {
		SquadList = new ArrayList<EntityEnnemy>();
		for (int r=0;r<row;r++) {
			for (int l=0;l<line;l++) {
				EntityEnnemy alien = new EntityEnnemy(sprite,new Vector2d(pos.x+(l*space.x),pos.y+r*space.y), 20,entitiesManager);
				SquadList.add(alien);
			}
		}
		return SquadList;
	}
}
