package level;

import java.util.ArrayList;

import misc.Position;
import entities.*;
import base.EntitiesManager;

public class LevelAlien extends Level {
		
	public LevelAlien(EntitiesManager entitiesManager) {
		super(entitiesManager, 3, 10, "sprites/alien.gif", 50, 50, new Position(100,50));
	}

	@Override
	public ArrayList<Entity> generateLevel() {
		nbCount = 0;
		SquadList = new ArrayList<Entity>();
		for (int r=0;r<row;r++) {
			for (int l=0;l<line;l++) {
				Entity alien = new EntityEnnemy(entitiesManager,sprite,pos.getX()+(l*spaceLR),pos.getY()+r*spaceTB, 1);
				SquadList.add(alien);
				nbCount++;
			}
		}
		return SquadList;
	}
	
	
	
}