package misc;
import entities.*;

import java.util.ArrayList;

import base.Game;

public abstract class Level {
	
	
	/**Number of row for the table*/
	private int row;
	
	/**Number of line of the table*/
	private int line;
	
	/**Path for the skin of the squad*/
	private String sprite;
	
	/**Distance between each entities left and right*/
	private int spaceLR;
	
	/**Distance between each entities top and bottom*/
	private int spaceTB;
	
	/**Numeber of entities in the squad*/
	private int nbCount;
	
	/**Array to generating squad*/
	ArrayList<Entity> SquadList;
	
	/**Position of thefirst ennemy (int the top left)*/
	private Position pos;
	
	public Level(int r, int l, String s, int spLR, int spTB,  Position p) {
		row = r;
		line = l;
		sprite = s;
		spaceLR = spLR;
		spaceTB = spTB;
		pos = p;
	}
	
	/**
	 * The function generate a table of ennemies
	 * @param g the game where ennemies are
	 */
	public ArrayList<Entity> generateLevel(Game g) {
		nbCount = 0;
		SquadList = new ArrayList<Entity>();
		for (int r=0;r<row;r++) {
			for (int l=0;l<line;l++) {
				Entity alien = new AlienEntity(g,sprite,pos.getX()+(line*spaceLR),pos.getY()+row*spaceTB);
				SquadList.add(alien);
				nbCount++;
			}
		}
		return SquadList;
	}
	
	public int getCount() {
		return nbCount;
	}
	
	/** This function remove 1 at nbCount when called */
	public boolean hasOneDestroyed() {
		
		//Si nbCount est déjà à 0
		if(nbCount==0) return false;
		
		nbCount--;
		return true;
	}
	
	public boolean isFinished() {
		return nbCount==0;
	}
}
