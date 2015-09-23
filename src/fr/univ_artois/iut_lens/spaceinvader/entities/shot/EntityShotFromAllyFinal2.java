package fr.univ_artois.iut_lens.spaceinvader.entities.shot;

import java.util.Random;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.Game;
import fr.univ_artois.iut_lens.spaceinvader.entities.CircleEntity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ship.ShipLimitedShot;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllyFinal2 extends EntityShotFromAlly implements CircleEntity {
	private long time = 0;
	private final ShipLimitedShot ship;
	private long duplicationInterval;
	private int nbOfChildPerDuplication;
	
	
	public EntityShotFromAllyFinal2(Vector2d p, Vector2d s,
			EntitiesManager eM, ShipLimitedShot ship, long dupInterval, int nbOfChild) {
		super("sprites/shotFinal.png", p, 10, 2, s, eM);
		this.ship = ship;
		duplicationInterval = dupInterval;
		nbOfChildPerDuplication = nbOfChild;
	}
	
	public void move(long delta) {
		super.move(delta);
		time++;
		if(time%duplicationInterval==0 && ship.getNbShotAlive()<ship.getMaxNbShot())  {
			time = 0;
			Random r = new Random();
			for (int i=0; i<nbOfChildPerDuplication; i++) {
			entitiesManager.getEntitiesList().add(new EntityShotFromAllyFinal2(new Vector2d(position.x, position.y), speed.add(new Vector2d(r.nextInt(400)-200,r.nextInt(400)-200)), entitiesManager, ship, duplicationInterval, nbOfChildPerDuplication));
			}
		}
		
		// colision sur la bordure de l'écranz
		if (position.x<0) {
			position = position.invertX(); // équivaut à position.x=-position.x;
			speed = speed.invertX();
		}
		if (position.x+sprite.getWidth()>Game.gameInstance.getWindowWidth()) {
			position.x -= (position.x+sprite.getWidth()-Game.gameInstance.getWindowWidth());
			speed = speed.invertX();
		}
		if (position.y<0) {
			position = position.invertY();
			speed = speed.invertY();
		}
		if (position.y+sprite.getHeight()>Game.gameInstance.getWindowHeight()) {
			position.y -= (position.y+sprite.getHeight()-Game.gameInstance.getWindowHeight());
			speed = speed.invertY();
		}
			
			
	}
}
