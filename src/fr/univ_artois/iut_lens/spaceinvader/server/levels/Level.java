package fr.univ_artois.iut_lens.spaceinvader.server.levels;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.move.StrategyMoveEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.strategy.shot.*;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

import java.util.ArrayList;
import java.util.List;

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
	private int nbCount;
	
	/**Array to generating squad*/
	protected ArrayList<EntityEnnemy> SquadList;
	
	private long maxEnemyLife = 0;
	
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
	protected abstract List<EntityEnnemy> generateLevel();
	
	
	public long getMaxEnemyLife() {
		return maxEnemyLife;
	}
	
	
	public List<EntityEnnemy> getNewlyGeneratedLevel() {
		List<EntityEnnemy> ents = generateLevel();
		nbCount = ents.size();
		maxEnemyLife = 0;
		for (EntityEnnemy e : ents) {
			maxEnemyLife += e.getLife();
		}
		return ents;
	}
	
	public int getCount() {
		return nbCount;
	}
	
	/** This function remove 1 at nbCount when called */
	public boolean hasOneEnnemyDestroyed() {
		
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
