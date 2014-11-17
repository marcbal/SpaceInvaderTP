package fr.univ_artois.iut_lens.spaceinvader.entities.shot;

import java.util.Random;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.Game;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllyFinal extends EntityShotFromAlly {
	int time = 0;
	private static int nbRange;
	public EntityShotFromAllyFinal(Vector2d p, Vector2d s,
			EntitiesManager eM) {
		super("sprites/UnderComplexShot.png", p, 4, 2, s, eM);
	}
	
	public void move(long delta) {
		super.move(delta);
		time++;
		if(time%50==0 && nbRange<Math.pow(2, 8))  {
			time = 0;
			nbRange++;
			entitiesManager.getEntitiesList().add(new EntityShotFromAllyFinal(new Vector2d(position.x-10, position.y+getBoundingBox().height/4.0), new Vector2d(-500,new Random().nextInt(1000)-500), entitiesManager));
			entitiesManager.getEntitiesList().add(new EntityShotFromAllyFinal(new Vector2d(position.x+10, position.y+getBoundingBox().height/4.0), new Vector2d(500,new Random().nextInt(1000)-500), entitiesManager));
			entitiesManager.getEntitiesList().add(new EntityShotFromAllyFinal(new Vector2d(position.x+getBoundingBox().width/4.0, position.y-10), new Vector2d(new Random().nextInt(1000)-500,-500), entitiesManager));
			entitiesManager.getEntitiesList().add(new EntityShotFromAllyFinal(new Vector2d(position.x+getBoundingBox().width/4.0, position.y+10), new Vector2d(new Random().nextInt(1000)-500,500), entitiesManager));
		}
		
		if(time==1000) time = 0;
		if(getPosition().x<10 || getPosition().x>790) speed = new Vector2d(speed.x*-1, speed.y);
		if(getPosition().y<10 || getPosition().y>590) speed = new Vector2d(speed.x, speed.y*-1);
	}
	
	@Override
	public void collidedWith(Entity other) {
		// prevents double kills, if we've already hit something,
		// don't collide
		if (used) {
			return;
		}
		
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
