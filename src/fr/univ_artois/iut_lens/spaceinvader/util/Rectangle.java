package fr.univ_artois.iut_lens.spaceinvader.util;

public class Rectangle {
	
	public double x, y, width, height;

	public Rectangle(double x, double y, double w, double h) {
		this.x = x;
		this.y = y;
		width = w;
		height = h;
	}
	
	public Vector2d getCenter() {
		return new Vector2d(x + width / 2, y + height / 2);
	}
	
	
	public boolean isInside(Rectangle r) {
		return (r.x >= x
				&& r.y >= y
				&& r.x + r.width <= x + width
				&& r.y + r.height <= y + height);
	}

}
