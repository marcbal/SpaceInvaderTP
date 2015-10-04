package fr.univ_artois.iut_lens.spaceinvader.server.entities.ship;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.shot.EntityShotFromAllyFinal2;
import fr.univ_artois.iut_lens.spaceinvader.server.players.ShipManager;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShipFinal2 extends EntityShip implements ShipLimitedShot {
	public final int nbMaxShotAlive;
	private long duplicationInterval;
	private int nbOfChildPerDuplication;
	private int nbShotAlive;
	

	public EntityShipFinal2(EntitiesManager eM, ShipManager sm, int fireInterval, int maxShot, long dupInterval, int nbOfChildPerDup) {
		super("sprites/FinalShip.png", 1000, eM, fireInterval, sm);
		nbMaxShotAlive = maxShot;
		duplicationInterval = dupInterval;
		nbOfChildPerDuplication = nbOfChildPerDup;
	}

	@Override
	public void shoot(long currentTime) {
			entitiesManager.getEntitiesList().add(new EntityShotFromAllyFinal2(new Vector2d(position.x+getBoundingBox().width/2-5, position.y), new Vector2d(0, -200), entitiesManager, this, duplicationInterval, nbOfChildPerDuplication));
	}
	
	
	public int getNbShotAlive() {
		return nbShotAlive;
	}

	@Override
	public int getMaxNbShot() {
		return nbMaxShotAlive;
	}

	@Override
	public void addAliveShot() {
		nbShotAlive++;
	}

	@Override
	public void removeAliveShot() {
		nbShotAlive--;
	}
	
	@Override
	public void reinitShot() {
		super.reinitShot();
		nbShotAlive = 0;
	}
}
