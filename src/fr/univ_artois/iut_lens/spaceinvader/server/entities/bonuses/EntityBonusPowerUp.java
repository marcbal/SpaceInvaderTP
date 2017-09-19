package fr.univ_artois.iut_lens.spaceinvader.server.entities.bonuses;

import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ship.EntityShip;
import fr.univ_artois.iut_lens.spaceinvader.server.players.ShipManager;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityBonusPowerUp extends EntityBonus {

	public EntityBonusPowerUp(Vector2d p, Vector2d s, EntitiesManager eM) {
		super("sprites/powerUp.png", p, s, eM);
	}
	
	@Override
	public synchronized void collidedWith(Entity other) {
		if(other instanceof EntityShip) {
			ShipManager shipMan = ((EntityShip)other).associatedShipManager;
			shipMan.increaseShipType();
			entitiesManager.remove(this);
			shipMan.getPlayer().addScore(30*(shipMan.getCurrentShipIndex()+1));
		}
	}

}
