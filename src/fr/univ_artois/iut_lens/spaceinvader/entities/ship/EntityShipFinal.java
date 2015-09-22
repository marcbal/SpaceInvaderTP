package fr.univ_artois.iut_lens.spaceinvader.entities.ship;

import java.util.concurrent.atomic.AtomicInteger;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShotFromAllyFinal;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShipFinal extends EntityShip implements ShipLimitedShot {
	public final int nbMaxShotAlive;
	private boolean hasShot = false;
	

	public EntityShipFinal(EntitiesManager eM, int maxShot) {
		super("sprites/FinalShip.png", 1000, eM, 1000000000000L);
		nbMaxShotAlive = maxShot;
	}

	@Override
	public void shoot(long currentTime) {
			entitiesManager.getEntitiesList().add(new EntityShotFromAllyFinal(new Vector2d(position.x+getBoundingBox().width/2-5, position.y), new Vector2d(0, -300), entitiesManager, this));
			hasShot = true;
	}
	
	
	public int getNbShotAlive() {
		AtomicInteger nb = new AtomicInteger(0);
		entitiesManager.getEntitiesList().forEach((entity) -> {
			if (entity instanceof EntityShotFromAllyFinal) nb.addAndGet(1);
		});
		return nb.get();
	}

	@Override
	public int getMaxNbShot() {
		return nbMaxShotAlive;
	}
	
	@Override
	protected boolean canShoot(long currentTime) {
		return !hasShot;
	}
	
	@Override
	public void reinitShot() {
		hasShot = false;
	}
}
