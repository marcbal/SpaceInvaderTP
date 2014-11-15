package fr.univ_artois.iut_lens.spaceinvader.levels;

import java.util.ArrayList;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.*;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy_move_strategy.StrategyMoveEnnemyNormal;
import fr.univ_artois.iut_lens.spaceinvader.util.Position;

public class LevelAlien extends Level {
		
	public LevelAlien(EntitiesManager entitiesManager) {
		super(entitiesManager,
				3,
				10,
				"sprites/alien.gif",
				50,
				50,
				new Position(100,50),
				new StrategyMoveEnnemyNormal());
	}

	@Override
	public ArrayList<Entity> generateLevel() {
		nbCount = 0;
		SquadList = new ArrayList<Entity>();
		for (int r=0;r<row;r++) {
			for (int l=0;l<line;l++) {
				Entity alien = new EntityEnnemy(sprite,pos.getX()+(l*spaceLR),pos.getY()+r*spaceTB, 1,entitiesManager);
				SquadList.add(alien);
				nbCount++;
			}
		}
		return SquadList;
	}
	
	
	
}