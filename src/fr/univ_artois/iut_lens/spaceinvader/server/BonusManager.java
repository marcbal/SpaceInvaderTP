package fr.univ_artois.iut_lens.spaceinvader.server;

import java.util.Random;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.bonuses.EntityBonusPowerDown;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.bonuses.EntityBonusPowerUp;
import fr.univ_artois.iut_lens.spaceinvader.sprites_manager.Sprite;
import fr.univ_artois.iut_lens.spaceinvader.sprites_manager.SpriteStore;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class BonusManager {
	private EntitiesManager entitiesManager;
	
	private Sprite sprUp = SpriteStore.get().getSprite("sprites/powerUp.png"),
			sprDown = SpriteStore.get().getSprite("sprites/powerDown.png");
	
	public BonusManager(EntitiesManager eM) {
		entitiesManager = eM;
	}
	
	public void performBonus() {
		Random r = new Random();
		
		if(r.nextInt(1750)<=5) {
			entitiesManager.getEntitiesList().add(new EntityBonusPowerUp(new Vector2d(r.nextInt(MegaSpaceInvader.DISPLAY_WIDTH-sprUp.getWidth()),r.nextInt(MegaSpaceInvader.DISPLAY_HEIGHT-300)), new Vector2d(0, r.nextInt(400)+20), entitiesManager));
		}
		if(r.nextInt(1750)<=5) {
			entitiesManager.getEntitiesList().add(new EntityBonusPowerDown(new Vector2d(r.nextInt(MegaSpaceInvader.DISPLAY_WIDTH-sprDown.getWidth()),r.nextInt(MegaSpaceInvader.DISPLAY_HEIGHT-300)), new Vector2d(0, r.nextInt(500)+10), entitiesManager));
		}
	}
}
