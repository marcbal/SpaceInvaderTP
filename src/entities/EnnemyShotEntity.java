package entities;

import base.Game;

public class EnnemyShotEntity extends ShotEntity {
	/** The vertical speed at which the players shot moves */
	private double moveSpeed = 300;
	
	/**
	 * Create a new shot from the player
	 * 
	 * @param game The game in which the shot has been created
	 * @param sprite The sprite representing this shot
	 * @param x The initial x location of the shot
	 * @param y The initial y location of the shot
	 */
	public EnnemyShotEntity(Game game,String sprite,int x,int y) {
		super(game, sprite,x,y);
		
		dy = moveSpeed;
	}
}
