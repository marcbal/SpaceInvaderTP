package fr.univ_artois.iut_lens.spaceinvader.client;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;

public class OnScreenDisplay {
	
	// c'est juste une valeur entière, sauf que là on évite les conflits de Thread
	private AtomicLong levelMaxLife = new AtomicLong(0);
	
	private KeyInputHandler keyHandler = Client.instance.getKeyInputHandler();
	
	/** The message to display which waiting for a key press */
	private AtomicReference<String> middleMessage = new AtomicReference<String>("");
	
	
	
	
	
	
	
	
	
	
	public void setLevelMaxLife(int mL) { levelMaxLife.set(mL); }
	public void setMiddleMessage(String m) { middleMessage.set(m); }
	
	
	
	
	
	
	
	public void drawOther(Graphics2D g) {
		Client client = Client.instance;
		long currentLife = 0;// TODO current ennemyLife;
		long maxLife = levelMaxLife.get();
		

		if (currentLife <= maxLife
				&& currentLife >= 0)
		{
			g.setColor(new Color((float)Math.sqrt(1-(currentLife/(float)maxLife)), (float)Math.sqrt(currentLife/(float)maxLife), 0F));
			g.fillRect(0, 0, (int)Math.ceil((currentLife/(float)maxLife)*MegaSpaceInvader.DISPLAY_WIDTH), 3);
		}
		int text_position_y = 0;
		int text_interval_y = 15;
		
		g.setColor(Color.WHITE);
		String[] mCommand = {"Commandes :", "[Gauche/Droite] : bouger", "[Espace] : tirer", "[Echap] : pause", "[F3] : infos"};
		for (String m : mCommand)
		{
			g.drawString(m, MegaSpaceInvader.DISPLAY_WIDTH - 5 - g.getFontMetrics().stringWidth(m), text_position_y+=text_interval_y);
		}
		
		text_position_y = 0;
		
		if (keyHandler.isKeyToggle("infos"))
		{
			g.drawString("Par Marc Baloup et Maxime Maroine, Groupe 2-C, IUT de Lens, DUT Informatique 2014-2015", 5, text_position_y+=text_interval_y);
			g.drawString("Sources : https://github.com/marcbal/SpaceInvaderTP", 5, text_position_y+=text_interval_y);
			
			int fpsGraphique = (int) Math.min(MegaSpaceInvader.CLIENT_FRAME_PER_SECOND, (1000000000/client.displayFrameDuration.get()));
			int fpsLogique = (int) Math.min(MegaSpaceInvader.SERVER_TICK_PER_SECOND, 0 /* TODO récupérer les TPS du serveur */);
			g.drawString("Threads collisions : "+MegaSpaceInvader.SERVER_NB_THREAD_FOR_ENTITY_COLLISION+" - FPS Graphique : "+fpsGraphique+" - FPS Logique : "+fpsLogique, 5, text_position_y+=text_interval_y);
			
			
			int nbEntities = 0; // TODO nombre d'entité
			g.drawString("Nombre d'entité : "+nbEntities, 5, text_position_y+=text_interval_y);
			g.drawString("Vie énemies : "+currentLife+"/"+maxLife, 5, text_position_y+=text_interval_y);
		}
		
		int[] gInfos = new int[2], gInfos2 = new int[2]; // TODO infos vaisseaux et niveaux
		g.drawString("Vaisseau : "+gInfos[0]+"/"+gInfos[1]+" - Niveau : "+gInfos2[0]+"/"+gInfos2[1], 5, MegaSpaceInvader.DISPLAY_HEIGHT-5);
		
	}
	
	public void drawMiddleWaiting(Graphics2D g)
	{
		String message = middleMessage.get();
		g.setColor(Color.WHITE);
		g.drawString(message,(MegaSpaceInvader.DISPLAY_WIDTH-g.getFontMetrics().stringWidth(message))/2,285);
		g.drawString("Appuyez sur Entrée",(MegaSpaceInvader.DISPLAY_WIDTH-g.getFontMetrics().stringWidth("Appuyez sur Entrée"))/2,315);
	}
	
	
	public void drawMiddlePause(Graphics2D g)
	{
		if (!keyHandler.isKeyToggle("pause")) return;
		g.setColor(Color.WHITE);
		g.drawString("--> PAUSE <--",(MegaSpaceInvader.DISPLAY_WIDTH-g.getFontMetrics().stringWidth("--> PAUSE <--"))/2,300);
	}
	
	
	
}
