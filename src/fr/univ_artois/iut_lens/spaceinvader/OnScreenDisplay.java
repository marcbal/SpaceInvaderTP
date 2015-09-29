package fr.univ_artois.iut_lens.spaceinvader;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import fr.univ_artois.iut_lens.spaceinvader.entities.ship.ShipLimitedShot;

public class OnScreenDisplay {
	
	// c'est juste une valeur entière, sauf que là on évite les conflits de Thread
	private AtomicLong levelMaxLife = new AtomicLong(0);
	
	private KeyInputHandler keyHandler = Game.gameInstance.getKeyInputHandler();
	
	/** The message to display which waiting for a key press */
	private AtomicReference<String> middleMessage = new AtomicReference<String>("");
	
	
	
	
	
	
	
	
	
	
	public void setLevelMaxLife(int mL) { levelMaxLife.set(mL); }
	public void setMiddleMessage(String m) { middleMessage.set(m); }
	
	
	
	
	
	
	
	public void drawOther(Graphics2D g) {
		Game game = Game.gameInstance;
		long currentLife = game.getEntitiesManager().getTotalRemainingEnnemyLife();
		long maxLife = levelMaxLife.get();
		

		if (currentLife <= maxLife
				&& currentLife >= 0)
		{
			g.setColor(new Color((float)Math.sqrt(1-(currentLife/(float)maxLife)), (float)Math.sqrt(currentLife/(float)maxLife), 0F));
			g.fillRect(0, 0, (int)Math.ceil((currentLife/(float)maxLife)*game.getWindowWidth()), 3);
		}
		int text_position_y = 0;
		int text_interval_y = 15;
		
		g.setColor(Color.WHITE);
		String[] mCommand = {"Commandes :", "[Gauche/Droite] : bouger", "[Espace] : tirer", "[Echap] : pause", "[F3] : infos"};
		for (String m : mCommand)
		{
			g.drawString(m, game.getWindowWidth() - 5 - g.getFontMetrics().stringWidth(m), text_position_y+=text_interval_y);
		}
		
		text_position_y = 0;
		
		if (keyHandler.isKeyToggle("infos"))
		{
			g.drawString("Par Marc Baloup et Maxime Maroine, Groupe 2-C, IUT de Lens, DUT Informatique 2014-2015", 5, text_position_y+=text_interval_y);
			g.drawString("Sources : https://github.com/marcbal/SpaceInvaderTP", 5, text_position_y+=text_interval_y);
			
			int fpsGraphique = (int) Math.min(game.fps, (1000000000/game.displayFrameDuration.get()));
			int fpsLogique = (int) Math.min(game.fps, (1000000000/game.logicalFrameDuration.get()));
			g.drawString("Threads collisions : "+game.entitiesManager.nbThread+" - FPS Graphique : "+fpsGraphique+" - FPS Logique : "+fpsLogique, 5, text_position_y+=text_interval_y);
			
			// infos vaisseau nombre limité de balle
			String dataShots = "";
			if (game.getShipManager().getCurrentShip() instanceof ShipLimitedShot) {
				ShipLimitedShot ship = (ShipLimitedShot) game.getShipManager().getCurrentShip();
				dataShots = " ("+ship.getNbShotAlive()+"/"+ship.getMaxNbShot()+")";
			}
			
			
			g.drawString("Nombre d'entité : "+game.getEntitiesManager().getEntitiesList().size()+dataShots, 5, text_position_y+=text_interval_y);
			g.drawString("Vie énemies : "+currentLife+"/"+maxLife, 5, text_position_y+=text_interval_y);
		}

		int[] gInfos = game.getShipManager().getShipProgress(), gInfos2 = game.getLevelManager().getLevelProgress();
		g.drawString("Vaisseau : "+gInfos[0]+"/"+gInfos[1]+" - Niveau : "+gInfos2[0]+"/"+gInfos2[1], 5, Game.gameInstance.getWindowHeight()-5);
		
	}
	
	public void drawMiddleWaiting(Graphics2D g)
	{
		Game game = Game.gameInstance;
		String message = middleMessage.get();
		g.setColor(Color.WHITE);
		g.drawString(message,(game.getWindowWidth()-g.getFontMetrics().stringWidth(message))/2,285);
		g.drawString("Appuyez sur Entrée",(game.getWindowWidth()-g.getFontMetrics().stringWidth("Appuyez sur Entrée"))/2,315);
	}
	
	
	public void drawMiddlePause(Graphics2D g)
	{
		Game game = Game.gameInstance;
		if (!keyHandler.isKeyToggle("pause")) return;
		g.setColor(Color.WHITE);
		g.drawString("--> PAUSE <--",(game.getWindowWidth()-g.getFontMetrics().stringWidth("--> PAUSE <--"))/2,300);
	}
	
	
	
}
