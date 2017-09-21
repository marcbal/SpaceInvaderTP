package fr.univ_artois.iut_lens.spaceinvader.server.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.Server;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ship.EntityShip;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ship.ShipLimitedShot;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllyFinal extends EntityShotFromAlly {
	long time = 0;
	
	private ShipLimitedShot limitedShip;
	
	
	public EntityShotFromAllyFinal(Vector2d p, Vector2d s,
			EntitiesManager eM, EntityShip ship) {
		super("sprites/UnderComplexShot.png", p, 10, 2, s, eM, ship);
		if (!(ship instanceof ShipLimitedShot))
			throw new ClassCastException("le vaisseau associé à ce tir doit être un vaisseau à tir actif limité (implements ShipLimitedShot)");
		limitedShip = (ShipLimitedShot)ship;
		limitedShip.addAliveShot();
	}
	
	@Override
	public void move(long delta) {
		super.move(delta);
		time++;
		if(time%20==0 && limitedShip.getNbShotAlive()<limitedShip.getMaxNbShot())  {
			time = 0;
			
			if (!entitiesManager.add(new EntityShotFromAllyFinal(new Vector2d(getPosition().x, getPosition().y), getSpeed().add(new Vector2d(-500,MegaSpaceInvader.RANDOM.nextInt(1000)-500)).minLength(300), entitiesManager, ship)))
				limitedShip.removeAliveShot();
			if (!entitiesManager.add(new EntityShotFromAllyFinal(new Vector2d(getPosition().x, getPosition().y), getSpeed().add(new Vector2d(500,MegaSpaceInvader.RANDOM.nextInt(1000)-500)).minLength(300), entitiesManager, ship)))
				limitedShip.removeAliveShot();
			if (!entitiesManager.add(new EntityShotFromAllyFinal(new Vector2d(getPosition().x, getPosition().y), getSpeed().add(new Vector2d(MegaSpaceInvader.RANDOM.nextInt(1000)-500,-500)).minLength(300), entitiesManager, ship)))
				limitedShip.removeAliveShot();
			if (!entitiesManager.add(new EntityShotFromAllyFinal(new Vector2d(getPosition().x, getPosition().y), getSpeed().add(new Vector2d(MegaSpaceInvader.RANDOM.nextInt(1000)-500,500)).minLength(300), entitiesManager, ship)))
				limitedShip.removeAliveShot();
		}
		
		// colision sur la bordure de l'écran
		if (getPosition().x<0) {
			setPosition(getPosition().invertX()); // équivaut à getPosition().x=-getPosition().x;
			setSpeed(getSpeed().invertX());
		}
		if (getPosition().x+sprite.getWidth()>MegaSpaceInvader.DISPLAY_WIDTH) {
			getPosition().x -= (getPosition().x+sprite.getWidth()-MegaSpaceInvader.DISPLAY_WIDTH);
			setSpeed(getSpeed().invertX());
		}
		if (getPosition().y<0) {
			setPosition(getPosition().invertY());
			setSpeed(getSpeed().invertY());
		}
		if (getPosition().y+sprite.getHeight()>MegaSpaceInvader.DISPLAY_HEIGHT) {
			getPosition().y -= (getPosition().y+sprite.getHeight()-MegaSpaceInvader.DISPLAY_HEIGHT);
			setSpeed(getSpeed().invertY());
		}
			
			
	}
	
	@Override
	public synchronized void collidedWith(Entity other) {
		// prevents double kills, if we've already hit something,
		// don't collide
		
		// if we've hit an alien, kill it!
		if (other instanceof EntityEnnemy) {
			ship.associatedShipManager.getPlayer().addScore(getDegat());
			// other prends les dégats donnés par le tir
			if (other.receiveDegat(this)) {
				// notify the game that the alien has been killed
				Server.serverInstance.notifyAlienKilled();
			}
		}

		//Si deux tirs se touchent (les 2 tirs dans les camps différents)
		if(other instanceof EntityShotFromEnnemy) {
			ship.associatedShipManager.getPlayer().addScore(1);
			entitiesManager.remove(other);
		}
	}
	
	
	@Override
	public void willDie() {
		limitedShip.removeAliveShot();
	}
}
