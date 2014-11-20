package fr.univ_artois.iut_lens.spaceinvader.entities.shot;

import java.util.Random;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.Game;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllyFinal extends EntityShotFromAlly {
	long time = 0;
	private static int nbRange;
	private static int maxRange = (int)Math.pow(2, 10);
	public EntityShotFromAllyFinal(Vector2d p, Vector2d s,
			EntitiesManager eM) {
		super("sprites/UnderComplexShot.png", p, 10, 2, s, eM);
	}
	
	public void move(long delta) {
		super.move(delta);
		time++;
		if(time%20==0 && nbRange<maxRange)  {
			time = 0;
			nbRange++;
			entitiesManager.getEntitiesList().add(new EntityShotFromAllyFinal(new Vector2d(position.x, position.y), new Vector2d(-500,new Random().nextInt(1000)-500), entitiesManager));
			entitiesManager.getEntitiesList().add(new EntityShotFromAllyFinal(new Vector2d(position.x, position.y), new Vector2d(500,new Random().nextInt(1000)-500), entitiesManager));
			entitiesManager.getEntitiesList().add(new EntityShotFromAllyFinal(new Vector2d(position.x, position.y), new Vector2d(new Random().nextInt(1000)-500,-500), entitiesManager));
			entitiesManager.getEntitiesList().add(new EntityShotFromAllyFinal(new Vector2d(position.x, position.y), new Vector2d(new Random().nextInt(1000)-500,500), entitiesManager));
		}
		
		// colision sur la bordure de l'écran
		if (position.x<0) {
			position.invertX(); // équivaut à position.x=-position.x;
			speed.invertX();
		}
		if (position.x+sprite.getWidth()>800) {
			position.x -= (position.x+sprite.getWidth()-800);
			speed.invertX();
		}
		if (position.y<0) {
			position.invertY();
			speed.invertY();
		}
		if (position.y+sprite.getHeight()>600) {
			position.y -= (position.y+sprite.getHeight()-600);
			speed.invertY();
		}
			
			
	}
	
	@Override
	public void collidedWith(Entity other) {
		// prevents double kills, if we've already hit something,
		// don't collide
		
		// if we've hit an alien, kill it!
		if (other instanceof EntityEnnemy) {
			// other prends les dégats donnés par le tir
			if (other.receiveDegat(this)) {
				// notify the game that the alien has been killed
				Game.gameInstance.notifyAlienKilled();
			}
		}

		//Si deux tirs se touchent (les 2 tirs dans les camps différents)
		if(other instanceof EntityShotFromEnnemy) {
			entitiesManager.removeEntity(other);
		}
	}
	
	public static void reset() {
		nbRange = 0;
	}
}
