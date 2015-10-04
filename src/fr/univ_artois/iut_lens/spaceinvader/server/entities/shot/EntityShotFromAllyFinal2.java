package fr.univ_artois.iut_lens.spaceinvader.server.entities.shot;

import java.util.Random;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ship.ShipLimitedShot;
import fr.univ_artois.iut_lens.spaceinvader.util.TargettingUtil;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllyFinal2 extends EntityShotFromAlly {
	private long time = 0;
	private final ShipLimitedShot ship;
	private long duplicationInterval;
	private int nbOfChildPerDuplication;

	private Entity target = null;
	
	
	public EntityShotFromAllyFinal2(Vector2d p, Vector2d s,
			EntitiesManager eM, ShipLimitedShot ship, long dupInterval, int nbOfChild) {
		super("sprites/shotFinal.png", p, 10, 2, s, eM);
		this.ship = ship;
		duplicationInterval = dupInterval;
		nbOfChildPerDuplication = nbOfChild;
		ship.addAliveShot();
	}
	
	public void move(long delta) {
		if (target == null || !entitiesManager.getEntitiesList().contains(target))
			target = TargettingUtil.searchTargetEnnemy(this, entitiesManager, Entity.class);
		if (target != null)
			speed = speed.add(TargettingUtil.getTargetDirection(this, target).dotProduct(0.05));
		
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
			speed = speed.invertX().dotProduct(0.6);
		}
		if (position.x+sprite.getWidth()>MegaSpaceInvader.DISPLAY_WIDTH) {
			position.x -= (position.x+sprite.getWidth()-MegaSpaceInvader.DISPLAY_WIDTH);
			speed = speed.invertX().dotProduct(0.6);
		}
		if (position.y<0) {
			position = position.invertY();
			speed = speed.invertY().dotProduct(0.6);
		}
		if (position.y+sprite.getHeight()>MegaSpaceInvader.DISPLAY_HEIGHT) {
			position.y -= (position.y+sprite.getHeight()-MegaSpaceInvader.DISPLAY_HEIGHT);
			speed = speed.invertY().dotProduct(0.6);
		}
			
	}
	
	@Override
	public void willDie() {
		ship.removeAliveShot();
	}
}
