package fr.univ_artois.iut_lens.spaceinvader.util;

public class Vector2d {
	/** Horizontal position from the left of the screen */
	public double x;
	
	/** Vertical position from the top of the screen */
	public double y;
	
	/**
	 * Constructor to set position
	 * @param x Coordinates left to right
	 * @param y Coordinates up to down
	 */
	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Default constructor
	 */
	public Vector2d() {
		this(0,0);
	}
	
	/**
	 * Copy constructor
	 */
	public Vector2d(Vector2d c) {
		this(c.x,c.y);
	}
	
	
	public Vector2d add(Vector2d v)
	{
		x += v.x;
		y += v.y;
		return this;
	}
	
	public Vector2d dup()
	{
		return new Vector2d(this);
	}
	
	public Vector2d dotProduct(double k)
	{
		x *= k;
		y *= k;
		return this;
	}
	
	public Vector2d invert()
	{
		x = -x;
		y = -y;
		return this;
	}
	
	public Vector2d invertX()
	{
		x = -x;
		return this;
	}
	
	public Vector2d invertY()
	{
		y = -y;
		return this;
	}
	
	public double distanceSquaredOf(Vector2d v)
	{
		return (x - v.x)*(x - v.x) + (y - v.y)*(y - v.y);
	}
	
	public double distanceOf(Vector2d v)
	{
		return Math.sqrt(distanceSquaredOf(v));
	}
}
