package misc;

import java.util.ArrayList;

import entities.AlienEntity;
import entities.Entity;
import base.Game;

/**
 * Class which made appear the horrible explosive Maxime !!
 *
 */
public class LevelMaxime extends Level {

	public LevelMaxime(Game g, int r, int l, int spLR, int spTB, Position p) {
		super(g, r, l,"sprites/max.jpg", spLR, spTB, p);
	}
	
	@Override
	public ArrayList<Entity> generateLevel() {
		nbCount = 0;
		SquadList = new ArrayList<Entity>();
		for (int r=0;r<row;r++) {
			for (int l=0;l<line;l++) {
				Entity alien = new AlienEntity(game,sprite,pos.getX()+(l*spaceLR),pos.getY()+r*spaceTB);
				SquadList.add(alien);
				nbCount++;
			}
		}
		return SquadList;
	}

}
