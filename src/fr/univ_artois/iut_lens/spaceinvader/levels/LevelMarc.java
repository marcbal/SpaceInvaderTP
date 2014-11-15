package fr.univ_artois.iut_lens.spaceinvader.levels;

import java.util.ArrayList;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.util.Position;

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
				Entity alien = new EntityEnnemy(sprite,pos.getX()+(l*spaceLR),pos.getY()+r*spaceTB, 2,entitiesManager);
				SquadList.add(alien);
				nbCount++;
			}
		}
		return SquadList;
	}
}