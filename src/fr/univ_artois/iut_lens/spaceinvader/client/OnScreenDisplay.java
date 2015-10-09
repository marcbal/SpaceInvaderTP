package fr.univ_artois.iut_lens.spaceinvader.client;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.concurrent.atomic.AtomicReference;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateInfos.GameInfo;

public class OnScreenDisplay {
	
	private KeyInputHandler keyHandler = Client.instance.getKeyInputHandler();
	
	/** The message to display which waiting for a key press */
	private AtomicReference<String> middleMessage = new AtomicReference<String>("");
	
	
	
	
	
	
	public void setMiddleMessage(String m) { middleMessage.set(m); }
	
	
	
	
	
	
	
	public void drawOther(Graphics2D g) {
		Client client = Client.instance;
		GameInfo serverInfos = Client.instance.lastGameInfo.get();
		
		long currentLife = serverInfos.currentEnemyLife;
		long maxLife = serverInfos.maxEnemyLife;
		

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
		
		if (keyHandler.isKeyToggled("infos"))
		{
			g.drawString("Par Marc Baloup et Maxime Maroine, Groupe 2-C, IUT de Lens, DUT Informatique 2014-2015", 5, text_position_y+=text_interval_y);
			g.drawString("Sources : https://github.com/marcbal/SpaceInvaderTP", 5, text_position_y+=text_interval_y);
			
			int fpsGraphique = (int) Math.min(MegaSpaceInvader.CLIENT_FRAME_PER_SECOND, (1000000000/client.displayFrameDuration.get()));
			int fpsLogique = (int) Math.min(serverInfos.maxTPS, 1000000000/(serverInfos.currentTickTime));
			g.drawString("Threads collisions : "+serverInfos.nbCollisionThreads+" - FPS Graphique : "+fpsGraphique+" - FPS Logique : "+fpsLogique, 5, text_position_y+=text_interval_y);
			
			
			int nbEntities = serverInfos.nbEntity;
			g.drawString("Nombre d'entité : "+nbEntities, 5, text_position_y+=text_interval_y);
			g.drawString("Vie énemies : "+currentLife+"/"+maxLife, 5, text_position_y+=text_interval_y);
		}
		
		int[] gInfos = new int[2], gInfos2 = new int[] {serverInfos.currentLevel, serverInfos.nbLevel}; // TODO infos vaisseaux
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
		if (!keyHandler.isKeyToggled("pause")) return;
		g.setColor(Color.WHITE);
		g.drawString("--> PAUSE <--",(MegaSpaceInvader.DISPLAY_WIDTH-g.getFontMetrics().stringWidth("--> PAUSE <--"))/2,300);
	}
	
	
	
	
	
}
