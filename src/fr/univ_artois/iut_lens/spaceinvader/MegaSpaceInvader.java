package fr.univ_artois.iut_lens.spaceinvader;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Random;

import javax.swing.UIManager;

import fr.univ_artois.iut_lens.spaceinvader.client.Client;
import fr.univ_artois.iut_lens.spaceinvader.launcher.LauncherDialog;
import fr.univ_artois.iut_lens.spaceinvader.launcher.LauncherDialog.LaunchingConfiguration;
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
	
	public static final int SERVER_NB_MAX_ENTITY = 7000;
	
	public static final Charset NETWORK_CHARSET = Charset.forName("UTF-8");
	
	public static final int NETWORK_TCP_BUFFER_SIZE = 1024*1024;
	
	
	
	
	
	
	
	
	public static final Random RANDOM = new Random();
	
	public static void main(String[] args) {
		shutdownHook();
		Thread.currentThread().setName("Main");
		

		try {
			// donne à l'interface graphique le thème associé au système d'exploitation
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		LauncherDialog diag = new LauncherDialog();
		diag.waitForDispose();
		
		LaunchingConfiguration launchConfig = diag.generateConfig();
		
		
		
		
		
		
		if (launchConfig.serverEnabled) {
			
			try {
				Server.initServer(launchConfig.serverPort, launchConfig.serverScoring);
			} catch (IOException e) {
				Logger.severe("Impossible de lancer le serveur :");
				e.printStackTrace();
			}
			
		}
		
		
		
		if (launchConfig.clientEnabled) {
			
			String addr = launchConfig.clientConnectionAddress;
			int port;
			String[] addrSplit = addr.split(":", 2);
			if (addrSplit.length <= 1) {
				addr = addrSplit[0];
				port = SERVER_DEFAULT_PORT;
			}
			else {
				addr = addrSplit[0];
				try {
					port = Integer.parseInt(addrSplit[1]);
				} catch(NumberFormatException e) {
					port = SERVER_DEFAULT_PORT;
				}
			}
			
			try {
				Client c = new Client(new InetSocketAddress(InetAddress.getByName(addr), port), launchConfig.clientPlayerName);
				c.start();
			} catch (IOException e) {
				Logger.severe("Impossible de lancer l'interface graphique :");
				e.printStackTrace();
			}
		
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
