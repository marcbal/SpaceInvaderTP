package level;

import java.util.ArrayList;

import misc.Position;
import entities.EntityEnnemy;
import entities.Entity;
import base.EntitiesManager;

/**
 * Class which made appear the terrifying Marc !!
 *
 */
public class LevelMarc extends Level {

	public LevelMarc(EntitiesManager entitiesManager) {
		super(entitiesManager, 4, 10,"sprites/marc.jpg", 50, 50, new Position(100, 50));
	}

	@Override
	public ArrayList<Entity> generateLevel() {
		nbCount = 0;
		SquadList = new ArrayList<Entity>();
		for (int r=0;r<row;r++) {
			for (int l=0;l<line;l++) {
				Entity alien = new EntityEnnemy(entitiesManager,sprite,pos.getX()+(l*spaceLR),pos.getY()+r*spaceTB, 4);
				SquadList.add(alien);
				nbCount++;
			}
		}
		return SquadList;
	}
}
