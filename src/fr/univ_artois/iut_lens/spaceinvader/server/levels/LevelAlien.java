package fr.univ_artois.iut_lens.spaceinvader.server.levels;

import java.util.ArrayList;

import fr.univ_artois.iut_lens.spaceinvader.entities.*;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.strategy.move.StrategyMoveEnnemyNormal;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.strategy.shot.StrategyShotEnnemyBasic;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
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
	public ArrayList<Entity> generateLevel() {
		nbCount = 0;
		SquadList = new ArrayList<Entity>();
		for (int r=0;r<row;r++) {
			for (int l=0;l<line;l++) {
				Entity alien = new EntityEnnemy(sprite,new Vector2d(pos.x+(l*space.x),pos.y+r*space.y), 2,entitiesManager);
				SquadList.add(alien);
				nbCount++;
			}
		}
		return SquadList;
	}
	
	
	
}