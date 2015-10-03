package fr.univ_artois.iut_lens.spaceinvader.server;

import java.io.IOException;

import fr.univ_artois.iut_lens.spaceinvader.server.network.ServerConnection;

public class ServerThread extends Thread {
	
	public static final int NB_THREAD_FOR_ENTITY_COLLISION = Runtime.getRuntime().availableProcessors();
	
	public final EntitiesManager entitiesManager = new EntitiesManager();
	
	public final LevelManager levelManager = new LevelManager(entitiesManager);
	
	public final ServerConnection serverConnection;
	
	public ServerThread(int port) throws IOException {
		super("Server Thread");
		
		serverConnection = new ServerConnection(port);
	}
	
	
	
	@Override
	public void run() {
		// lancement du serveur
		
		
		
	}

}
