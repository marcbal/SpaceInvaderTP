package fr.univ_artois.iut_lens.spaceinvader.server.entities.shot;

import java.util.Random;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ship.EntityShip;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ship.ShipLimitedShot;
import fr.univ_artois.iut_lens.spaceinvader.util.TargettingUtil;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllyFinal2 extends EntityShotFromAlly {
	private long time = 0;
	private long duplicationInterval;
	private int nbOfChildPerDuplication;

	private Entity target = null;
	
	private ShipLimitedShot limitedShip;
	
	
	public EntityShotFromAllyFinal2(Vector2d p, Vector2d s,
			EntitiesManager eM, EntityShip ship, long dupInterval, int nbOfChild) {
		super("sprites/shotFinal.png", p, 10, 2, s, eM, ship);
		if (!(ship instanceof ShipLimitedShot))
			throw new ClassCastException("le vaisseau associé à ce tir doit être un vaisseau à tir actif limité (implements ShipLimitedShot)");
		limitedShip = (ShipLimitedShot)ship;
		duplicationInterval = dupInterval;
		nbOfChildPerDuplication = nbOfChild;
		limitedShip.addAliveShot();
	}
	
	@Override
	public void move(long delta) {
		if (target == null || target.plannedToRemoved())
			target = TargettingUtil.searchTargetEnnemy(this, entitiesManager, Entity.class, true);
		if (target != null)
			setSpeed(getSpeed().add(TargettingUtil.getTargetDirection(this, target).dotProduct(0.05)));
		
		super.move(delta);
		time++;
		if(time%duplicationInterval==0 && limitedShip.getNbShotAlive()<limitedShip.getMaxNbShot())  {
			time = 0;
			Random r = MegaSpaceInvader.RANDOM;
			for (int i=0; i<nbOfChildPerDuplication; i++) {
				if (!entitiesManager.add(new EntityShotFromAllyFinal2(new Vector2d(getPosition().x, getPosition().y), getSpeed().add(new Vector2d(r.nextInt(400)-200,r.nextInt(400)-200)), entitiesManager, ship, duplicationInterval, nbOfChildPerDuplication))) {
					limitedShip.removeAliveShot();
					break;
				}
			}
		}
		// colision sur la bordure de l'écran
		if (getPosition().x<0) {
			setPosition(getPosition().invertX()); // équivaut à position.x=-position.x;
			setSpeed(getSpeed().invertX().dotProduct(0.6));
		}
		if (getPosition().x+sprite.getWidth()>MegaSpaceInvader.DISPLAY_WIDTH) {
			getPosition().x -= (getPosition().x+sprite.getWidth()-MegaSpaceInvader.DISPLAY_WIDTH);
			setSpeed(getSpeed().invertX().dotProduct(0.6));
		}
		if (getPosition().y<0) {
			setPosition(getPosition().invertY());
			setSpeed(getSpeed().invertY().dotProduct(0.6));
		}
		if (getPosition().y+sprite.getHeight()>MegaSpaceInvader.DISPLAY_HEIGHT) {
			getPosition().y -= (getPosition().y+sprite.getHeight()-MegaSpaceInvader.DISPLAY_HEIGHT);
			setSpeed(getSpeed().invertY().dotProduct(0.6));
		}
			
	}
	
	@Override
	public void willDie() {
		limitedShip.removeAliveShot();
	}
}
