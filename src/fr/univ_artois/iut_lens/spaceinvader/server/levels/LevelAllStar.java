package fr.univ_artois.iut_lens.spaceinvader.server.levels;

import java.util.ArrayList;
import java.util.List;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.move.StrategyMoveEnnemyBossPremier;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.move.StrategyMoveEnnemyDisturbed;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.shot.StrategyShotEnnemyAimFor;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class LevelAllStar extends Level {
		
	public LevelAllStar(EntitiesManager entitiesManager) {
		super(entitiesManager,
				1,
				3,
				"sprites/alien_spaceship_by_animot-d5t4j61.png",
				new Vector2d(200,50),
				new Vector2d(75,50),
				new StrategyMoveEnnemyBossPremier(),
				new StrategyShotEnnemyAimFor(50));
	}

	@Override
	public List<EntityEnnemy> generateLevel() {
		SquadList = new ArrayList<EntityEnnemy>();
		for (int r=0;r<row;r++) {
			for (int l=0;l<line;l++) {
				if((l+1)%3==1) {
					EntityEnnemy alien = new EntityEnnemy("sprites/marc.jpg", new Vector2d(pos.x+(l*space.x),pos.y+r*space.y),1000,entitiesManager);
					SquadList.add(alien);
				}
				else if((l+1)%2==0) {
					EntityEnnemy alien = new EntityEnnemy("sprites/max.jpg",new Vector2d(pos.x+(l*space.x),pos.y+r*space.y),1250,entitiesManager);
					SquadList.add(alien);
				}
				else {
					EntityEnnemy alien = new EntityEnnemy(sprite,new Vector2d(pos.x+(l*space.x),pos.y+r*space.y),1500,entitiesManager);
					SquadList.add(alien);
				}
			}
		}
		return SquadList;
	}
	
	public boolean hasOneDestroyed() {
		
		boolean ret = super.hasOneDestroyed();
		if(getCount()==2) {
			strategyMove = new StrategyMoveEnnemyDisturbed();
			strategyShot = new StrategyShotEnnemyAimFor(15);
		}
		return ret;
		
	}
	
}