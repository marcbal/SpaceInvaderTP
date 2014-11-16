package fr.univ_artois.iut_lens.spaceinvader.entities.ship;

import fr.univ_artois.iut_lens.spaceinvader.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShotFromAllyAdvanced;
import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShotFromAllyBasic;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityShipSupership extends EntityShip {
	protected long lastFireTime = 0; //Dernier tir du vaisseau
	private long fireInterval = 50; //Intervalle de temps par défaut pour lequel le vaisseau peut tirer

	public EntityShipSupership(EntitiesManager eM) {
		super("sprites/supership1.png", 400, eM);
	}

	@Override
	public void tryToShoot(long currentTime) {
		if(currentTime - lastFireTime < fireInterval) return; //L'interval de tir est trop court
		lastFireTime = currentTime; //On met le dernier tire au temps actuel
		
		entitiesManager.getEntitiesList().add(new EntityShotFromAllyAdvanced(new Vector2d(position.x+getBoundingBox().width/2-20, position.y), new Vector2d(0, -300), entitiesManager));
		entitiesManager.getEntitiesList().add(new EntityShotFromAllyBasic(new Vector2d(position.x+getBoundingBox().width/2-10, position.y), new Vector2d(-150, -1000), entitiesManager));
		entitiesManager.getEntitiesList().add(new EntityShotFromAllyBasic(new Vector2d(position.x+getBoundingBox().width/2, position.y), new Vector2d(150, -1000), entitiesManager));
		entitiesManager.getEntitiesList().add(new EntityShotFromAllyAdvanced(new Vector2d(position.x+getBoundingBox().width/2+10, position.y), new Vector2d(0, -300), entitiesManager));

	}

}
