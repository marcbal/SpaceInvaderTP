package fr.univ_artois.iut_lens.spaceinvader.util;

public class Position {
	/** Horizontal position from the left of the screen */
	private double x;
	
	/** Vertical position from the top of the screen */
	private double y;
	
	/**
	 * Constructor to set position
	 * @param x Coordinates left to right
	 * @param y Coordinates up to down
	 */
	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Default constructor
	 */
	public Position() {
		this(0,0);
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY()  {
		return y;
	}
}
