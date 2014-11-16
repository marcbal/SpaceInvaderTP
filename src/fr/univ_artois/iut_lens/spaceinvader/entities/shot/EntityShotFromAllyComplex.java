package fr.univ_artois.iut_lens.spaceinvader.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.Game;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllyComplex extends EntityShotFromAlly {

	double i = 0;
	int split; //Ecart entre chaque tir
	public EntityShotFromAllyComplex(Vector2d p, EntitiesManager eM, int s) {
		super("sprites/ComplexShot.png", p, 100, 50, new Vector2d(0, -150), eM);
		split = s;
	}
	
	public void move(long delta) {
		super.move(delta);
		entitiesManager.getEntitiesList().add(new EntityShotFromAllyBasic(position, new Vector2d(Math.cos(i)*300,Math.sin(i)*300), entitiesManager));
		i += Math.PI/split;
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
			
			// remove the affected entities
			entitiesManager.removeEntity(this);
			// other prends les dégats donnés par le tir
			if (other.receiveDegat(this))
				// notify the game that the alien has been killed
				Game.gameInstance.notifyAlienKilled();
		}

		//Si deux tirs se touchent (les 2 tirs dans les camps différents)
		if(other instanceof EntityShotFromEnnemy) {
			entitiesManager.removeEntity(other);
		}
	}
	
}
