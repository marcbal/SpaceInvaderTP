package fr.univ_artois.iut_lens.spaceinvader.client;

import fr.univ_artois.iut_lens.spaceinvader.sprites_manager.Sprite;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EntityRepresenter {
	
	private Vector2d position;
	private Vector2d speed;
	private String name;
	private Sprite sprite;
	private int currentLife = 0;
	private int maxLife = 0;
	private long lastUpdateNanoTime;
	
	public EntityRepresenter(Sprite spr, Vector2d pos, Vector2d sp, String n, int currLife, int maxLife, long updateTime) {
		setPosition(pos);
		setSpeed(sp);
		setName(n);
		setCurrentLife(currLife);
		setMaxLife(maxLife);
		setUpdateNanoTime(updateTime);
		sprite = spr;
	}

	public Vector2d getPosition() { return position; }

	public void setPosition(Vector2d pos) { position = pos; }

	public Vector2d getSpeed() { return speed; }

	public void setSpeed(Vector2d s) { speed = s; }

	public String getName() { return name; }

	public void setName(String n) { name = n; }

	public int getCurrentLife() { return currentLife; }

	public void setCurrentLife(int curLife) { currentLife = curLife; }

	public int getMaxLife() { return maxLife; }

	public void setMaxLife(int maxLife) { this.maxLife = maxLife; }
	
	public long getUpdateNanoTime() { return lastUpdateNanoTime; }
	
	public void setUpdateNanoTime(long updateTime) { lastUpdateNanoTime = updateTime; }
	

	
	/**
	 * Draw this entity to the graphics context provided
	 * 
	 * @param g The graphics context on which to draw
	 * @param loopStart 
	 */
	public void draw(GraphicsContext g, long loopStart, long defaultTickDuration, float slowdownCoeff) {
		long supposedElapsedTickTime = Math.min((long) ((loopStart - lastUpdateNanoTime)/slowdownCoeff), defaultTickDuration);
		double posX = position.x+supposedElapsedTickTime*speed.x/1000000000;
		double posY = position.y+supposedElapsedTickTime*speed.y/1000000000;
		
		g.drawImage(sprite.getImage(), posX, posY);
		
		if (currentLife!=maxLife && currentLife > 0) {
			g.setFill(Color.DARKGRAY);
			g.fillRect(posX, posY, sprite.getWidth(), 3);
			g.setFill(new Color(Math.sqrt(1-(currentLife/(float)maxLife)), Math.sqrt(currentLife/(float)maxLife), 0, 1));
			g.fillRect(posX, posY,  (sprite.getWidth()*(currentLife/(double)maxLife)), 3);
		}
		
		if (name != null && name.length()>0) {
			g.setFill(Color.WHITE);
			g.fillText(name, posX, posY + 5);
		}
		
	}

}
