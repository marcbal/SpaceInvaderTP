package fr.univ_artois.iut_lens.spaceinvader.client;

import java.awt.Color;
import java.awt.Graphics;

import fr.univ_artois.iut_lens.spaceinvader.sprites_manager.Sprite;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

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
	 * @param loop_start 
	 */
	public void draw(Graphics g, long loop_start, long max_interval, float coeff) {
		long timeInterval = Math.min(loop_start - lastUpdateNanoTime, max_interval);
		double posX = position.x+timeInterval*coeff*speed.x/1000000000;
		double posY = position.y+timeInterval*coeff*speed.y/1000000000;
		
		sprite.draw(g,(int)posX,(int)posY);

		if (currentLife!=maxLife && currentLife > 0) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect((int)posX,(int)posY, sprite.getWidth(), 3);
			g.setColor(new Color((float)Math.sqrt(1-(currentLife/(float)maxLife)), (float)Math.sqrt(currentLife/(float)maxLife), 0F));
			g.fillRect((int)posX,(int)posY, (int)((double)sprite.getWidth()*(currentLife/(double)maxLife)), 3);
		}
		
		if (name != null && name.length()>0) {
			g.setColor(Color.WHITE);
			g.drawString(name,(int)posX,(int)posY+5);
		}
		
	}

}
