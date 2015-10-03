package fr.univ_artois.iut_lens.spaceinvader.entities.bonuses;

import fr.univ_artois.iut_lens.spaceinvader.ShipManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ship.EntityShip;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityBonusPowerUp extends EntityBonus {

	public EntityBonusPowerUp(Vector2d p, Vector2d s, EntitiesManager eM, ShipManager sM) {
		super("sprites/powerUp.png", p, s, eM, sM);
	}
	
	public synchronized void collidedWith(Entity other) {
		if(other instanceof EntityShip) {
			shipManager.increaseShipType();
			entitiesManager.removeEntity(this);
		}
	}

}
