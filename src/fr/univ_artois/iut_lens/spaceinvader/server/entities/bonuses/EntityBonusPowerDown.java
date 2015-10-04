package fr.univ_artois.iut_lens.spaceinvader.server.entities.bonuses;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ship.EntityShip;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityBonusPowerDown extends EntityBonus {

	public EntityBonusPowerDown(Vector2d p, Vector2d s, EntitiesManager eM) {
		super("sprites/powerDown.png", p, s, eM);
	}
	
	public synchronized void collidedWith(Entity other) {
		if(other instanceof EntityShip) {
			((EntityShip)other).associatedShipManager.decreaseShipType();
			entitiesManager.removeEntity(this);
		}
	}

}
