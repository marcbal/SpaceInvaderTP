package fr.univ_artois.iut_lens.spaceinvader.entities.ship;

import java.util.concurrent.atomic.AtomicInteger;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShotFromAllyFinal2;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShipFinal2 extends EntityShip implements ShipLimitedShot {
	public final int nbMaxShotAlive;
	private long duplicationInterval;
	private int nbOfChildPerDuplication;
	

	public EntityShipFinal2(EntitiesManager eM, int fireInterval, int maxShot, long dupInterval, int nbOfChildPerDup) {
		super("sprites/FinalShip.png", 1000, eM, fireInterval);
		nbMaxShotAlive = maxShot;
		duplicationInterval = dupInterval;
		nbOfChildPerDuplication = nbOfChildPerDup;
	}

	@Override
	public void shoot(long currentTime) {
			entitiesManager.getEntitiesList().add(new EntityShotFromAllyFinal2(new Vector2d(position.x+getBoundingBox().width/2-5, position.y), new Vector2d(0, -300), entitiesManager, this, duplicationInterval, nbOfChildPerDuplication));
	}
	
	
	public int getNbShotAlive() {
		AtomicInteger nb = new AtomicInteger(0);
		entitiesManager.getEntitiesList().forEach((entity) -> {
			if (entity instanceof EntityShotFromAllyFinal2) nb.addAndGet(1);
		});
		return nb.get();
	}

	@Override
	public int getMaxNbShot() {
		return nbMaxShotAlive;
	}
}
