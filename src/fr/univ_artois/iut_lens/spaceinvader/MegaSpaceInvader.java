package fr.univ_artois.iut_lens.spaceinvader;

import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Random;

import fr.univ_artois.iut_lens.spaceinvader.client.Client;
import fr.univ_artois.iut_lens.spaceinvader.launcher.CommandArgsParser;
import fr.univ_artois.iut_lens.spaceinvader.launcher.ConfigurationSaver;
import fr.univ_artois.iut_lens.spaceinvader.launcher.LauncherDialog;
import fr.univ_artois.iut_lens.spaceinvader.launcher.LaunchingConfiguration;
import fr.univ_artois.iut_lens.spaceinvader.server.Server;
import fr.univ_artois.iut_lens.spaceinvader.sprites_manager.SpriteStore;
import fr.univ_artois.iut_lens.spaceinvader.util.Logger;
import javafx.application.Application;
import javafx.stage.Stage;

public class MegaSpaceInvader extends Application {
	
	/*
	 * Global configuration
	 * _ /!\ : Some values may affect game behaviour if there
	 * are different between the server and the client instance
	 */
	public static final int DISPLAY_WIDTH = 960;
	public static final int DISPLAY_HEIGHT = 540;
	
	public static final int SERVER_DEFAULT_PORT = 34567;
	
	public static final Charset NETWORK_CHARSET = Charset.forName("UTF-8");
	
	public static final int NETWORK_TCP_BUFFER_SIZE = 1024*1024;
	
	public static final int NETWORK_TIMEOUT = 10*1000; // 10 secondes
	
	public static final int NETWORK_NB_ENTITY_PER_UPDATE_PACKET = 1300;
	
	/*
	 * Client configuration (doesn't affect server)
	 */
	public static final int CLIENT_FRAME_PER_SECOND = 120;
	
	public static final boolean CLIENT_SMOOTH_ENTITY_MOVEMENT = true; // usefull if framerate is greater than server tickrate
	
	/*
	 * Server configuration (doesn't affect client)
	 */
	public static final int SERVER_NB_THREAD_FOR_ENTITY_COLLISION = Runtime.getRuntime().availableProcessors();
	
	public static final int SERVER_NB_MAX_ENTITY = 4000;
	
	public static final int SERVER_TICK_PER_SECOND = 30;
	
	
	
	
	
	
	
	
	public static final Random RANDOM = new Random();
	
	
	public static boolean headless;
	private static LaunchingConfiguration launchConfig = null;
	private static Stage primaryStage;
	
	// paramètres : [-s [PORT_NUMBER [score]]] [-c NICKNAME [SERVER_ADDR:PORT]]
	/*
	 * -s : activer le serveur local
	 * 		PORT_NUMBER : numéro de port attribué au serveur (par défaut 34567)
	 * 		'score' pour activer le système de score (désactivé par défaut)
	 * -c : activer le client local
	 * 		NICKNAME le pseudo du joueur
	 * 		SERVER_ADDR:PORT : adresse de connexion au serveur. Ignoré si le serveur local est démarré, obligatoire sinon.
	 */
	
	public static void main(String[] args) {
		shutdownHook();
		Thread.currentThread().setName("Main");
		
		headless = GraphicsEnvironment.isHeadless();
		
		
		
		
		// analyse de la ligne de commande.
		try {
			launchConfig = CommandArgsParser.getConfigurationFromArgs(args);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if (headless) {
			
			if (launchConfig == null) {
				showCommandLineHelp();
				System.exit(1);
			}
			else {
				postLauncher();
			}
			
		}
		else {
			// run JavaFX
			Application.launch(args);
		}
		
		
		
	}
	
	
	
	@Override
	public void start(Stage stage) throws Exception {
		
		primaryStage = stage;
		
		primaryStage.getIcons().add(SpriteStore.get().getSprite("sprites/ComplexShot.png").getImage());
		primaryStage.setResizable(false);
		
		// si la ligne de commande ne donne rien de concluant, affichage du launcher.
		if (launchConfig == null) {
			
			
			LauncherDialog diag = new LauncherDialog(primaryStage);
			
			LaunchingConfiguration savedConfig = new ConfigurationSaver().getConfigFromFile();
			if (savedConfig != null)
				diag.applyConfiguration(savedConfig);
			
		}
		else {
			postLauncher();
		}
	}
	
	
	public static void afterLauncherUI(LaunchingConfiguration config) {
		
		launchConfig = config;

		new ConfigurationSaver().saveConfiguration(launchConfig);
		
		postLauncher();
	}
	
	
	private static void postLauncher() {
		
		if (launchConfig.serverEnabled) {
			
			try {
				Server.initServer(launchConfig.serverPort, launchConfig.serverScoring);
			} catch (IOException e) {
				Logger.severe("Impossible de lancer le serveur :");
				e.printStackTrace();
			}
			
		}
		
		
		
		if (launchConfig.clientEnabled && !headless) {
			
			
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
				new Client(primaryStage, new InetSocketAddress(InetAddress.getByName(addr), port), launchConfig.clientPlayerName);
			} catch (IOException e) {
				Logger.severe("Impossible de lancer l'interface graphique :");
				e.printStackTrace();
			}
		
		}
		
	}
	
	
	
	private static void showCommandLineHelp() {
		System.out.println("paramètres : [-s [PORT_NUMBER [score]]] [-c NICKNAME [SERVER_ADDR:PORT]]");
		System.out.println();
		System.out.println("-s : activer le serveur local");
		System.out.println("    PORT_NUMBER : numéro de port attribué au serveur (par défaut 34567)");
		System.out.println("    'score' pour activer le système de score (désactivé par défaut)");
		System.out.println("-c : activer le client local (ignoré si le mode headless est activé)");
		System.out.println("    NICKNAME le pseudo du joueur");
		System.out.println("    SERVER_ADDR:PORT : adresse de connexion au serveur. Ignoré si le serveur local est démarré, obligatoire sinon.");
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
