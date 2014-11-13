package misc;
import entities.*;

import java.util.ArrayList;

import base.EntitiesManager;

/**
 * This class generate levels, it's an abstract class
 */
public abstract class Level {
	
	protected EntitiesManager entitiesManager;
	
	/**Number of row for the table*/
	protected int row;
	
	/**Number of line of the table*/
	protected int line;
	
	/**Path for the skin of the squad*/
	protected String sprite;
	
	/** Position of the first alien in the top left*/
	protected Position pos;
	
	/**Distance between each entities left and right*/
	protected int spaceLR;
	
	/**Distance between each entities top and bottom*/
	protected int spaceTB;
	
	/**Numeber of entities in the squad*/
	protected int nbCount;
	
	/**Array to generating squad*/
	ArrayList<Entity> SquadList;
	
	public Level(EntitiesManager eM, int r, int l, String s, int spLR, int spTB,  Position p) {
		
		entitiesManager = eM;
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
	public abstract ArrayList<Entity> generateLevel();
	
	public int getCount() {
		return nbCount;
	}
	
	/** This function remove 1 at nbCount when called */
	public boolean hasOneDestroyed() {
		
		//If the last alien as been killed
		if(nbCount==1) return false;
		
		nbCount--;
		return true;
	}
	
	public boolean isFinished() {
		return nbCount==0;
	}
}
