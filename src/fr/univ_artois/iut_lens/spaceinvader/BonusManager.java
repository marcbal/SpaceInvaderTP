package fr.univ_artois.iut_lens.spaceinvader;

import java.util.Random;

import fr.univ_artois.iut_lens.spaceinvader.entities.bonuses.EntityBonusPowerDown;
import fr.univ_artois.iut_lens.spaceinvader.entities.bonuses.EntityBonusPowerUp;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class BonusManager {
	private EntitiesManager entitiesManager;
	private ShipManager shipManager;
	
	private Sprite sprUp = SpriteStore.get().getSprite("sprites/powerUp.png"),
			sprDown = SpriteStore.get().getSprite("sprites/powerDown.png");
	
	public BonusManager(EntitiesManager eM, ShipManager sM) {
		entitiesManager = eM;
		shipManager = sM;
	}
	
	public void performBonus() {
		Random r = new Random();
		
		if(r.nextInt(1750)<=5) {
			entitiesManager.getEntitiesList().add(new EntityBonusPowerUp(new Vector2d(r.nextInt(Game.gameInstance.getWindowWidth()-sprUp.getWidth()),r.nextInt(Game.gameInstance.getWindowHeight()-300)), new Vector2d(0, r.nextInt(400)+20), entitiesManager, shipManager));
		}
		if(r.nextInt(1750)<=5) {
			entitiesManager.getEntitiesList().add(new EntityBonusPowerDown(new Vector2d(r.nextInt(Game.gameInstance.getWindowWidth()-sprDown.getWidth()),r.nextInt(Game.gameInstance.getWindowHeight()-300)), new Vector2d(0, r.nextInt(500)+10), entitiesManager, shipManager));
		}
	}
}
