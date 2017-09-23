package fr.univ_artois.iut_lens.spaceinvader.sprites_manager;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * A sprite to be displayed on the screen. Note that a sprite
 * contains no state information, i.e. its just the image and 
 * not the location. This allows us to use a single sprite in
 * lots of different places without having to store multiple 
 * copies of the image.
 * 
 * @author Kevin Glass
 */
public class Sprite {
	private static int identifierIncrement = 0;
	
	private Image image = null;
	public final int id;
	
	/**
	 * Create a new sprite based on an image
	 * 
	 * @param image The image that is this sprite
	 */
	public Sprite(Image bI) {
		image = bI;
		
		id = identifierIncrement++;
	}
	
	/**
	 * Get the width of the drawn sprite
	 * 
	 * @return The width in pixels of this sprite
	 */
	public int getWidth() {
		return (int)image.getWidth();
	}

	/**
	 * Get the height of the drawn sprite
	 * 
	 * @return The height in pixels of this sprite
	 */
	public int getHeight() {
		return (int)image.getHeight();
	}
	
	
	
	/**
	 * 
	 * @return l'image à afficher, ou null si on est en mode headless
	 */
	public Image getImage() {
		return image;
	}
	
	
	
	/**
	 * Draw the sprite onto the graphics context provided
	 * 
	 * @param g The graphics context on which to draw the sprite
	 * @param x The x location at which to draw the sprite
	 * @param y The y location at which to draw the sprite
	 */
	public void draw(GraphicsContext g, double x, double y) {
		g.drawImage(image, x, y);
	}
}