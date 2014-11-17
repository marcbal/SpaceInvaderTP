package fr.univ_artois.iut_lens.spaceinvader.entities.ship;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShotFromAllyFinal;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShipFinal extends EntityShip {
	protected long lastFireTime = 0; //Dernier tir du vaisseau
	private long fireInterval = 0; //Intervalle de temps par défaut pour lequel le vaisseau peut tirer
	public static boolean shoot = false;

	public EntityShipFinal(EntitiesManager eM) {
		super("sprites/FinalShip.png", 1000, eM);
	}

	@Override
	public void tryToShoot(long currentTime) {
		if(!shoot) {
			shoot = true;
			if(currentTime - lastFireTime < fireInterval) return; //L'interval de tir est trop court
			lastFireTime = currentTime; //On met le dernier tire au temps actuel
			
			entitiesManager.getEntitiesList().add(new EntityShotFromAllyFinal(new Vector2d(position.x+getBoundingBox().width/2-5, position.y), new Vector2d(0, -300), entitiesManager));
		}
	}
	public static void reset() {
		shoot = false;
	}
}
