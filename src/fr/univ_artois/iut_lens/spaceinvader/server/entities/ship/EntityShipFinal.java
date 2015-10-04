package fr.univ_artois.iut_lens.spaceinvader.server.entities.ship;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.shot.EntityShotFromAllyFinal;
import fr.univ_artois.iut_lens.spaceinvader.server.players.ShipManager;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShipFinal extends EntityShip implements ShipLimitedShot {
	public final int nbMaxShotAlive;
	private boolean hasShot = false;
	private int nbShotAlive = 0;
	

	public EntityShipFinal(EntitiesManager eM, ShipManager sm, int maxShot) {
		super("sprites/FinalShip.png", 1000, eM, 1000000000000L, sm);
		nbMaxShotAlive = maxShot;
	}

	@Override
	public void shoot(long currentTime) {
			entitiesManager.getEntitiesList().add(new EntityShotFromAllyFinal(new Vector2d(position.x+getBoundingBox().width/2-5, position.y), new Vector2d(0, -300), entitiesManager, this));
			hasShot = true;
	}
	
	@Override
	public int getNbShotAlive() {
		return nbShotAlive;
	}
	
	@Override
	public void addAliveShot() {
		nbShotAlive++;
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
		nbShotAlive = 0;
	}

	@Override
	public void removeAliveShot() {
		nbShotAlive--;
	}
}
