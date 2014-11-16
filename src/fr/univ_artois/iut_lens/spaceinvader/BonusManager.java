package fr.univ_artois.iut_lens.spaceinvader;

import java.util.Random;

import fr.univ_artois.iut_lens.spaceinvader.entities.bonuses.EntityBonusPowerDown;
import fr.univ_artois.iut_lens.spaceinvader.entities.bonuses.EntityBonusPowerUp;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class BonusManager {
	private EntitiesManager entitiesManager;
	private ShipManager shipManager;
	
	public BonusManager(EntitiesManager eM, ShipManager sM) {
		entitiesManager = eM;
		shipManager = sM;
	}
	
	public void performBonus() {
		Random r1 = new Random(); //Pour les powers up
		Random r2 = new Random(); //Pour les powers Down
		
		//Position
		Random x = new Random();
		Random y = new Random();
		
		//Vitesse
		Random sY = new Random();
		
		if(r1.nextInt(1000)<=5) {
			entitiesManager.getEntitiesList().add(new EntityBonusPowerUp(new Vector2d(x.nextInt(800),y.nextInt(300)), new Vector2d(0, sY.nextInt(300)+10), entitiesManager, shipManager));
		}
		if(r2.nextInt(1000)<=5) {
			entitiesManager.getEntitiesList().add(new EntityBonusPowerDown(new Vector2d(x.nextInt(800),y.nextInt(300)), new Vector2d(0, sY.nextInt(300)+10), entitiesManager, shipManager));
		}
	}
}
