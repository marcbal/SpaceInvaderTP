package fr.univ_artois.iut_lens.spaceinvader.sprites_manager;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

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
	
	private final BufferedImage bImage;
	/** The image to be drawn for this sprite */
	private Image image = null;
	public final int id;
	
	/**
	 * Create a new sprite based on an image
	 * 
	 * @param image The image that is this sprite
	 */
	public Sprite(BufferedImage bI) {
		bImage = bI;
		
		if (SpriteStore.graphicsConfig != null) {
			// create an accelerated image of the right size to store our sprite in
			image = SpriteStore.graphicsConfig.createCompatibleImage(bI.getWidth(),bI.getHeight(),Transparency.TRANSLUCENT);
			// draw our source image into the accelerated image
			image.getGraphics().drawImage(bI,0,0,null);
		}
		
		id = identifierIncrement++;
	}
	
	/**
	 * Get the width of the drawn sprite
	 * 
	 * @return The width in pixels of this sprite
	 */
	public int getWidth() {
		return bImage.getWidth();
	}

	/**
	 * Get the height of the drawn sprite
	 * 
	 * @return The height in pixels of this sprite
	 */
	public int getHeight() {
		return bImage.getHeight();
	}
	
	
	
	/**
	 * 
	 * @return l'image à afficher, ou null si on est en mode headless
	 */
	public Image getAWTImage() {
		return image;
	}
	
	
	
	/**
	 * 
	 * @return le buffer contenant l'image
	 */
	public BufferedImage getBufferedImage() {
		return bImage;
	}
	
	/**
	 * Draw the sprite onto the graphics context provided
	 * 
	 * @param g The graphics context on which to draw the sprite
	 * @param x The x location at which to draw the sprite
	 * @param y The y location at which to draw the sprite
	 */
	public void draw(Graphics g,int x,int y) {
		g.drawImage(image,x,y,null);
	}
}