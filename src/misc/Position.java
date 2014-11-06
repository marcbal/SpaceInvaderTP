package misc;

public class Position {
	/** Coordinates position in X on the screen */
	protected int x;
	
	/** Coordinates position in Y on the screen */
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
	 * Constructor by default
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
