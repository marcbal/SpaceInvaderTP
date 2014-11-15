package fr.univ_artois.iut_lens.spaceinvader.util;

public class Position {
	/** Horizontal position from the left of the screen */
	protected int x;
	
	/** Vertical position from the top of the screen */
	protected int y;
	
	/**
	 * Constructor to set position
	 * @param x Coordinates left to right
	 * @param y Coordinates up to down
	 */
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Default constructor
	 */
	public Position() {
		this(0,0);
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY()  {
		return y;
	}
}
