package fr.univ_artois.iut_lens.spaceinvader.levels;

import java.util.ArrayList;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.StrategyMoveEnnemyNormal;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.StrategyMoveEnnemyRandom;
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
				50,
				50,
				new Vector2d(50, 50),
				new StrategyMoveEnnemyNormal());
	}

	@Override
	public ArrayList<Entity> generateLevel() {
		nbCount = 0;
		SquadList = new ArrayList<Entity>();
		for (int r=0;r<row;r++) {
			for (int l=0;l<line;l++) {
				Entity alien = new EntityEnnemy(sprite,new Vector2d(pos.x+(l*spaceLR),pos.y+r*spaceTB), 5,entitiesManager);
				SquadList.add(alien);
				nbCount++;
			}
		}
		return SquadList;
	}
	
	public boolean hasOneDestroyed() {
		
		boolean ret = super.hasOneDestroyed();
		
		if (nbCount == 40)
			strategyMove = new StrategyMoveEnnemyRandom();
		
		return ret;
		
	}
}
