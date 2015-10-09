package fr.univ_artois.iut_lens.spaceinvader.server.entities.shot;

import java.util.Random;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.Server;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ship.ShipLimitedShot;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllyFinal extends EntityShotFromAlly {
	long time = 0;
	private final ShipLimitedShot ship;
	
	
	public EntityShotFromAllyFinal(Vector2d p, Vector2d s,
			EntitiesManager eM, ShipLimitedShot ship) {
		super("sprites/UnderComplexShot.png", p, 10, 2, s, eM);
		this.ship = ship;
		ship.addAliveShot();
	}
	
	public void move(long delta) {
		super.move(delta);
		time++;
		if(time%20==0 && ship.getNbShotAlive()<ship.getMaxNbShot())  {
			time = 0;
			entitiesManager.add(new EntityShotFromAllyFinal(new Vector2d(position.x, position.y), speed.add(new Vector2d(-500,new Random().nextInt(1000)-500)), entitiesManager, ship));
			entitiesManager.add(new EntityShotFromAllyFinal(new Vector2d(position.x, position.y), speed.add(new Vector2d(500,new Random().nextInt(1000)-500)), entitiesManager, ship));
			entitiesManager.add(new EntityShotFromAllyFinal(new Vector2d(position.x, position.y), speed.add(new Vector2d(new Random().nextInt(1000)-500,-500)), entitiesManager, ship));
			entitiesManager.add(new EntityShotFromAllyFinal(new Vector2d(position.x, position.y), speed.add(new Vector2d(new Random().nextInt(1000)-500,500)), entitiesManager, ship));
		}
		
		// colision sur la bordure de l'écran
		if (position.x<0) {
			position = position.invertX(); // équivaut à position.x=-position.x;
			speed = speed.invertX();
		}
		if (position.x+sprite.getWidth()>MegaSpaceInvader.DISPLAY_WIDTH) {
			position.x -= (position.x+sprite.getWidth()-MegaSpaceInvader.DISPLAY_WIDTH);
			speed = speed.invertX();
		}
		if (position.y<0) {
			position = position.invertY();
			speed = speed.invertY();
		}
		if (position.y+sprite.getHeight()>MegaSpaceInvader.DISPLAY_HEIGHT) {
			position.y -= (position.y+sprite.getHeight()-MegaSpaceInvader.DISPLAY_HEIGHT);
			speed = speed.invertY();
		}
			
			
	}
	
	@Override
	public synchronized void collidedWith(Entity other) {
		// prevents double kills, if we've already hit something,
		// don't collide
		
		// if we've hit an alien, kill it!
		if (other instanceof EntityEnnemy) {
			// other prends les dégats donnés par le tir
			if (other.receiveDegat(this)) {
				// notify the game that the alien has been killed
				Server.serverInstance.notifyAlienKilled();
			}
		}

		//Si deux tirs se touchent (les 2 tirs dans les camps différents)
		if(other instanceof EntityShotFromEnnemy) {
			entitiesManager.remove(other);
		}
	}
	
	
	@Override
	public void willDie() {
		ship.removeAliveShot();
	}
}
