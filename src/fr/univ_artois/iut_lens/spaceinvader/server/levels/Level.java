package fr.univ_artois.iut_lens.spaceinvader.server.levels;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.*;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.move.StrategyMoveEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.shot.*;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

import java.util.ArrayList;

/**
 * This class generate levels, it's an abstract class
 */
public abstract class Level {
	
	protected EntitiesManager entitiesManager;
	
	
	protected StrategyMoveEnnemy strategyMove;
	
	protected StrategyShotEnnemy strategyShot;
	
	/**Number of row for the table*/
	protected int row;
	
	/**Number of line of the table*/
	protected int line;
	
	/**Path for the skin of the squad*/
	protected String sprite;
	
	/** Position of the first alien in the top left*/
	protected Vector2d pos;
	
	/**Distance between each entities*/
	protected Vector2d space;
	
	/**Numeber of entities in the squad*/
	protected int nbCount;
	
	/**Array to generating squad*/
	ArrayList<Entity> SquadList;
	
	public Level(EntitiesManager eM, int r, int l, String s, Vector2d sp,  Vector2d p, StrategyMoveEnnemy stratMove, StrategyShotEnnemy stratShot) {
		
		entitiesManager = eM;
		row = r;
		line = l;
		sprite = s;
		space = sp;
		pos = p;
		
		strategyMove = stratMove;
		strategyShot = stratShot;
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
		
		nbCount--;
		
		return (nbCount>0);
	}
	
	public boolean isFinished() {
		return nbCount==0;
	}
	
	public StrategyMoveEnnemy getCurrentStrategyMove()
	{
		return strategyMove;
	}
	
	public StrategyShotEnnemy getCurrentStrategyShot() {
		return strategyShot;
	}
}
