package fr.univ_artois.iut_lens.spaceinvader.sprites_manager;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import fr.univ_artois.iut_lens.spaceinvader.util.Logger;

/**
 * A resource manager for sprites in the game. Its often quite important
 * how and where you get your game resources from. In most cases
 * it makes sense to have a central resource loader that goes away, gets
 * your resources and caches them for future use.
 * <p>
 * [singleton]
 * <p>
 * @author Kevin Glass
 */
public class SpriteStore {
	/** The single instance of this class */
	private static SpriteStore single = new SpriteStore();
	
	static GraphicsConfiguration graphicsConfig = null;
	
	static {
		try {
			graphicsConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		} catch (HeadlessException e) { }
	}
	
	/**
	 * Get the single instance of this class 
	 * 
	 * @return The single instance of this class
	 */
	public static SpriteStore get() {
		return single;
	}
	
	/** The cached sprite map, from reference to sprite instance */
	private Map<String, Sprite> sprites = new HashMap<>();
	
	/** Les sprites nouvellement chargés */
	private Map<String, Sprite> newSprites = new HashMap<>();
	
	/**
	 * Retrieve a sprite from the store
	 * 
	 * @param ref The reference to the image to use for the sprite
	 * @return A sprite instance containing an accelerate image of the request reference
	 */
	public synchronized Sprite getSprite(String ref) {
		// if we've already got the sprite in the cache
		// then just return the existing version
		if (sprites.get(ref) != null) {
			return sprites.get(ref);
		}
		
		// otherwise, go away and grab the sprite from the resource
		// loader
		BufferedImage sourceImage = null;
		
		try {
			// The ClassLoader.getResource() ensures we get the sprite
			// from the appropriate place, this helps with deploying the game
			// with things like webstart. You could equally do a file look
			// up here.
			URL url = this.getClass().getClassLoader().getResource(ref);
			
			if (url == null) {
				fail("Can't find ref: "+ref);
			}
			
			// use ImageIO to read the image in
			sourceImage = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to load: "+ref);
		}
		
		// create a sprite, add it the cache then return it
		Sprite sprite = new Sprite(sourceImage);
		
		sprites.put(ref,sprite);
		newSprites.put(ref, sprite);
		
		
		return sprite;
	}
	

	public synchronized Map<String, Sprite> getAllSprites() {
		return Collections.unmodifiableMap(sprites);
	}
	
	public synchronized Map<String, Sprite> getAllNewSprites() {
		Map<String, Sprite> ret = newSprites;
		newSprites = new HashMap<>();
		return ret;
	}
	
	public synchronized Map<Integer, String> getSpritesId(boolean onlyNew) {
		Map<String, Sprite> spritesData = onlyNew ? getAllNewSprites() : getAllSprites();
		Map<Integer, String> spritesId = new HashMap<>();
		for (Entry<String, Sprite> sp : spritesData.entrySet()) {
			spritesId.put(sp.getValue().id, sp.getKey());
		}
		return spritesId;
	}
	
	
	/**
	 * Utility method to handle resource loading failure
	 * 
	 * @param message The message to display on failure
	 */
	private void fail(String message) {
		// we're pretty dramatic here, if a resource isn't available
		// we dump the message and exit the game
		Logger.severe(message);
		System.exit(0);
	}
}