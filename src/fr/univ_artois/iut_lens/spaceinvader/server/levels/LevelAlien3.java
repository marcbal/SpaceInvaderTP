package fr.univ_artois.iut_lens.spaceinvader.server.levels;

import java.util.ArrayList;
import java.util.List;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.move.StrategyMoveEnnemyBossPremier;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.move.StrategyMoveEnnemyDisturbed;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.shot.StrategyShotEnnemyAimFor;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class LevelAlien3 extends Level {
		
	public LevelAlien3(EntitiesManager entitiesManager) {
		super(entitiesManager,
				2,
				2,
				"sprites/alien_spaceship_by_animot-d5t4j611.png",
				new Vector2d(50,50),
				new Vector2d(200,50),
				new StrategyMoveEnnemyBossPremier(),
				new StrategyShotEnnemyAimFor(100));
	}

	@Override
	public List<EntityEnnemy> generateLevel() {
		SquadList = new ArrayList<>();
		for (int r=0;r<row;r++) {
			for (int l=0;l<line;l++) {
				EntityEnnemy alien = new EntityEnnemy(sprite,new Vector2d(pos.x+(l*space.x),pos.y+r*space.y),300,entitiesManager);
				SquadList.add(alien);
			}
		}
		return SquadList;
	}
	
	@Override
	public boolean hasOneEnnemyDestroyed() {
		
		boolean ret = super.hasOneEnnemyDestroyed();
		if(getCount()==2) {
			strategyMove = new StrategyMoveEnnemyDisturbed();
		}
		return ret;
		
	}
	
}