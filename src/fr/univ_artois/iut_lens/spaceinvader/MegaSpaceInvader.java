package fr.univ_artois.iut_lens.spaceinvader;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import fr.univ_artois.iut_lens.spaceinvader.client.Client;
import fr.univ_artois.iut_lens.spaceinvader.server.Server;
import fr.univ_artois.iut_lens.spaceinvader.util.Logger;

public class MegaSpaceInvader {
	
	/*
	 * 
	 * Global configuration
	 * 
	 */
	public static final int CLIENT_FRAME_PER_SECOND = 120;
	
	public static final boolean CLIENT_SMOOTH_ENTITY_MOVEMENT = true; // usefull if framerate is greater than server tickrate
	
	public static final int SERVER_NB_THREAD_FOR_ENTITY_COLLISION = Runtime.getRuntime().availableProcessors();
	
	public static final int SERVER_TICK_PER_SECOND = 30;

	public static final int DISPLAY_WIDTH = 960;
	public static final int DISPLAY_HEIGHT = 540;
	
	public static final int SERVER_DEFAULT_PORT = 34567;
	
	public static final Charset NETWORK_CHARSET = Charset.forName("UTF-8");
	
	
	
	public static void main(String[] args) {
		shutdownHook();
		Thread.currentThread().setName("Main");
		
		// ici, mettre une interface de configuration du serveur (avec Java Swing, ça serai bien :3 )
		
		try {
			Server.initServer(SERVER_DEFAULT_PORT);
		} catch (IOException e) {
			Logger.severe("Impossible de lancer le serveur :");
			e.printStackTrace();
		}
		
		
		String addr = (args.length > 0) ? args[0] : "localhost";
		
		
		try {
			Client c = new Client(new InetSocketAddress(InetAddress.getByName(addr), SERVER_DEFAULT_PORT), "Test");
			c.start();
		} catch (IOException e) {
			Logger.severe("Impossible de lancer l'interface graphique :");
			e.printStackTrace();
		}
		
		
		
		
		
		
	}
	
	
	
	
	
	private static void shutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				Server.serverInstance.gameRunning.set(false);
			} catch(Exception e) { }
			try {
				Client.instance.gameRunning.set(false);
			} catch(Exception e) { }
		}));
	}
	
	
	
	
}
