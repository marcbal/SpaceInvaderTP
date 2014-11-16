package fr.univ_artois.iut_lens.spaceinvader.entities.ship;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShotFromAllyBasic;
import fr.univ_artois.iut_lens.spaceinvader.util.Position;

public class EntityShipPowered extends EntityShip {
	protected long lastFireTime = 0; //Dernier tir du vaisseau
	private long fireInterval = 100; //Intervalle de temps par défaut pour lequel le vaisseau peut tirer

	public EntityShipPowered(EntitiesManager eM) {
		super("sprites/spaceship1.png", 300, eM);
	}

	@Override
	public void tryToShoot(long currentTime) {
		if(currentTime - lastFireTime < fireInterval) return; //L'interval de tir est trop court
		lastFireTime = currentTime; //On met le dernier tire au temps actuel
		
		entitiesManager.getEntitiesList().add(new EntityShotFromAllyBasic(getX()+getBoundingBox().width/2-10, getY(), new Position(-100, -1000), entitiesManager));
		entitiesManager.getEntitiesList().add(new EntityShotFromAllyBasic(getX()+getBoundingBox().width/2, getY(), new Position(100, -1000), entitiesManager));

		
	}

}
