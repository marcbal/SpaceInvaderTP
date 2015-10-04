package fr.univ_artois.iut_lens.spaceinvader;

import java.io.IOException;
import java.net.InetSocketAddress;

import fr.univ_artois.iut_lens.spaceinvader.client.Client;
import fr.univ_artois.iut_lens.spaceinvader.server.Server;
import fr.univ_artois.iut_lens.spaceinvader.util.Logger;

public class MegaSpaceInvader {
	
	/*
	 * 
	 * Global configuration
	 * 
	 */
	public static final int CLIENT_FRAME_PER_SECOND = 60;
	
	public static final int SERVER_NB_THREAD_FOR_ENTITY_COLLISION = Runtime.getRuntime().availableProcessors();
	
	public static final int SERVER_TICK_PER_SECOND = 30;

	public static final int DISPLAY_WIDTH = 960;
	public static final int DISPLAY_HEIGHT = 540;
	
	public static final int SERVER_DEFAULT_PORT = 34567;
	
	
	
	
	public static void main(String[] args) {
		Thread.currentThread().setName("Main");
		
		// ici, mettre une interface de configuration du serveur (avec Java Swing, ça serai bien :3 )
		
		try {
			Server.initServer(SERVER_DEFAULT_PORT);
		} catch (IOException e) {
			Logger.severe("Impossible de lancer le serveur :");
			e.printStackTrace();
		}
		
		
		try {
			Client c = new Client(new InetSocketAddress("localhost", SERVER_DEFAULT_PORT), "Test");
			c.start();
		} catch (IOException e) {
			Logger.severe("Impossible de lancer l'interface graphique :");
			e.printStackTrace();
		}
		
		
		
		
		
		
	}
	
	
	
	
}
