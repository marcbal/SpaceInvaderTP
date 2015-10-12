package fr.univ_artois.iut_lens.spaceinvader.client;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.concurrent.atomic.AtomicReference;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateInfos.GameInfo;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateInfos.GameInfo.PlayerInfo;
import fr.univ_artois.iut_lens.spaceinvader.util.DataSizeUtil;

public class OnScreenDisplay {
	
	private KeyInputHandler keyHandler = Client.instance.getKeyInputHandler();
	
	/** The message to display which waiting for a key press */
	private AtomicReference<String> middleMessage = new AtomicReference<String>("");
	
	
	
	
	
	
	public void setMiddleMessage(String m) { middleMessage.set(m); }
	
	
	
	
	
	
	
	public void drawOtherInfos(Graphics2D g) {
		Client client = Client.instance;
		GameInfo serverInfos = Client.instance.lastGameInfo.get();
		
		boolean displayDetails = keyHandler.isKeyToggled("infos");
		
		/*
		 * Indicateur de vie énemie, en haut de l'écran
		 */
		long currentLife = serverInfos.currentEnemyLife;
		long maxLife = serverInfos.maxEnemyLife;
		if (currentLife <= maxLife
				&& currentLife >= 0) {
			g.setColor(new Color((float)Math.sqrt(1-(currentLife/(float)maxLife)), (float)Math.sqrt(currentLife/(float)maxLife), 0F));
			g.fillRect(0, 0, (int)Math.ceil((currentLife/(float)maxLife)*MegaSpaceInvader.DISPLAY_WIDTH), 3);
		}
		
		g.setColor(Color.WHITE);
		String[] mCommand = {"Commandes :", "[Gauche/Droite] : bouger", "[Espace] : tirer", "[Echap] : pause", "[F3] : infos"};
		int txtLine = 0;
		for (String m : mCommand) {
			drawAlignedString(g, m, TextHorizontalAlign.RIGHT, TextVerticalAlign.TOP, txtLine++);
		}
		
		/*
		 * Texte en haut à gauche : Crédits, informations sur l'interface graphique
		 */
		if (displayDetails) {
			drawAlignedString(g, "Par Maxime Maroine et Marc Baloup, DUT Informatique, IUT de Lens, 2014-2015", TextHorizontalAlign.LEFT, TextVerticalAlign.TOP, 0);
			drawAlignedString(g, "Sources : https://github.com/marcbal/SpaceInvaderTP", TextHorizontalAlign.LEFT, TextVerticalAlign.TOP, 1);

			int fpsGraphique = (int) Math.min(MegaSpaceInvader.CLIENT_FRAME_PER_SECOND, (1000000000/client.displayFrameDuration.get()));
			drawAlignedString(g, "Framerate : "+fpsGraphique+"/s", TextHorizontalAlign.LEFT, TextVerticalAlign.TOP, 2);

		}
		
		/*
		 * Texte en bas à droite : progression des joueurs
		 */
		int[] gInfos = new int[] {serverInfos.currentShip, serverInfos.nbShip}, gInfos2 = new int[] {serverInfos.currentLevel, serverInfos.nbLevel};
		drawAlignedString(g, "Votre vaisseau : "+gInfos[0]+"/"+gInfos[1]+" - Flotte énemie : "+gInfos2[0]+"/"+gInfos2[1], TextHorizontalAlign.RIGHT, TextVerticalAlign.BOTTOM, 0);
		
		
		txtLine = 1;
		for (PlayerInfo pInfo : serverInfos.playerInfos) {
			if (displayDetails) {
				drawAlignedString(g, "Up="+DataSizeUtil.humanReadableByteCount(pInfo.upBandwidth, false)+"/s Down="+DataSizeUtil.humanReadableByteCount(pInfo.downBandwidth, false)+"/s Ping="+(pInfo.ping/1000000)+"ms - "+pInfo.name+" : "+pInfo.score, TextHorizontalAlign.RIGHT, TextVerticalAlign.BOTTOM, txtLine++);
			}
			else {
				drawAlignedString(g, pInfo.name+" : "+pInfo.score, TextHorizontalAlign.RIGHT, TextVerticalAlign.BOTTOM, txtLine++);
			}
		}
		drawAlignedString(g, "Joueurs :", TextHorizontalAlign.RIGHT, TextVerticalAlign.BOTTOM, txtLine++);
		if (keyHandler.isKeyToggled("infos")) {
			drawAlignedString(g, "Vie énemies : "+currentLife+"/"+maxLife, TextHorizontalAlign.RIGHT, TextVerticalAlign.BOTTOM, txtLine++);
		}
		/*
		 * Texte en bas à gauche : infos du serveur
		 */
		if (displayDetails) {
			int fpsLogique = (int) Math.min(serverInfos.maxTPS, 1000000000/(serverInfos.currentTickTime));
			int nbEntities = serverInfos.nbEntity;
			drawAlignedString(g, "Threads collisions : "+serverInfos.nbCollisionThreads+" - Tickrate : "+fpsLogique+"/s - Nombre d'entité : "+nbEntities, TextHorizontalAlign.LEFT, TextVerticalAlign.BOTTOM, 0);
			
			// TODO afficher le log du serveur
		}
	}
	
	public void drawMiddleWaiting(Graphics2D g)
	{
		String message = middleMessage.get();
		g.setColor(Color.WHITE);
		drawAlignedString(g, message, TextHorizontalAlign.CENTER, TextVerticalAlign.MIDDLE, -1);
		drawAlignedString(g, "Appuyez sur Entrée", TextHorizontalAlign.CENTER, TextVerticalAlign.MIDDLE, 1);
	}
	
	
	public void drawMiddlePause(Graphics2D g)
	{
		if (!keyHandler.isKeyToggled("pause")) return;
		g.setColor(Color.WHITE);
		drawAlignedString(g, "--> PAUSE <--", TextHorizontalAlign.CENTER, TextVerticalAlign.MIDDLE, 0);
	}
	
	
	
	
	private void drawAlignedString(Graphics2D g, String text, TextHorizontalAlign hAlign, TextVerticalAlign vAlign, int offset) {
		// border margin
		int margin = 5;
		// space between lines
		int lineHeight = 12;
		
		text = text.trim();
		
		int y = (vAlign == TextVerticalAlign.BOTTOM)
				? MegaSpaceInvader.DISPLAY_HEIGHT-margin-lineHeight*offset
				: (vAlign == TextVerticalAlign.TOP)
				? margin+lineHeight*(offset+1)
				: MegaSpaceInvader.DISPLAY_HEIGHT/2+lineHeight/2+lineHeight*offset;
		int x = (hAlign == TextHorizontalAlign.LEFT)
				? margin
				: (hAlign == TextHorizontalAlign.RIGHT)
				? MegaSpaceInvader.DISPLAY_WIDTH-5-g.getFontMetrics().stringWidth(text)
				: MegaSpaceInvader.DISPLAY_WIDTH/2-g.getFontMetrics().stringWidth(text)/2;
		
		g.drawString(text, x, y);
		
		
		
	}
	

	private static enum TextHorizontalAlign {
		LEFT, RIGHT, CENTER
	}
	private static enum TextVerticalAlign {
		TOP, MIDDLE, BOTTOM
	}
	
	
	
	
	
}
