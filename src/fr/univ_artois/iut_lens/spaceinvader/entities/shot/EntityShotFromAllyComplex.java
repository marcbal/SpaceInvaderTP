package fr.univ_artois.iut_lens.spaceinvader.entities.shot;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.Game;
import fr.univ_artois.iut_lens.spaceinvader.entities.CircleEntity;
import fr.univ_artois.iut_lens.spaceinvader.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShotFromAllyComplex extends EntityShotFromAlly implements CircleEntity {

	double i = 0;
	double tourParSeconde = 4;
	public EntityShotFromAllyComplex(Vector2d p, EntitiesManager eM) {
		super("sprites/ComplexShot.png", p, 50, 50, new Vector2d(0, -50), eM);
	}
	
	public void move(long delta) {
		super.move(delta);
		entitiesManager.getEntitiesList().add(new EntityShotFromAllySubComplex(new Vector2d(position.x+getBoundingBox().width/2.0-5, position.y+getBoundingBox().height/2.0-5), new Vector2d((Math.cos(i)*300),(Math.sin(i)*300)), entitiesManager));
		entitiesManager.getEntitiesList().add(new EntityShotFromAllySubComplex2(new Vector2d(position.x+getBoundingBox().width/2.0-5, position.y+getBoundingBox().height/2.0-5), new Vector2d((Math.cos(Math.PI+i)*300),(Math.sin(Math.PI+i)*300)), entitiesManager));
		
		i += tourParSeconde*2*Math.PI*(delta/1000000000D);
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
				degat++;
			}
		}

		//Si deux tirs se touchent (les 2 tirs dans les camps différents)
		if(other instanceof EntityShotFromEnnemy) {
			entitiesManager.removeEntity(other);
		}
	}
	
}
