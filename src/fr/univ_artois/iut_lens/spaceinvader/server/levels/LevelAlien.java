package fr.univ_artois.iut_lens.spaceinvader.server.levels;

import java.util.ArrayList;
import java.util.List;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.move.StrategyMoveEnnemyNormal;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.shot.StrategyShotEnnemyBasic;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class LevelAlien extends Level {
		
	public LevelAlien(EntitiesManager entitiesManager) {
		super(entitiesManager,
				3,
				10,
				"sprites/alien_spaceship_by_animot-d5t4j611.png",
				new Vector2d(50,100),
				new Vector2d(100,50),
				new StrategyMoveEnnemyNormal(),
				new StrategyShotEnnemyBasic(3000));
	}

	@Override
	public List<EntityEnnemy> generateLevel() {
		SquadList = new ArrayList<EntityEnnemy>();
		for (int r=0;r<row;r++) {
			for (int l=0;l<line;l++) {
				EntityEnnemy alien = new EntityEnnemy(sprite,new Vector2d(pos.x+(l*space.x),pos.y+r*space.y), 2,entitiesManager);
				SquadList.add(alien);
			}
		}
		return SquadList;
	}
	
	
	
}