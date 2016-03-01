package fr.univ_artois.iut_lens.spaceinvader.util;

import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerLog;
import fr.univ_artois.iut_lens.spaceinvader.server.Server;

public class Logger {
	
	
	
	// private static final List<String> messages = new ArrayList<String>();
	

	public synchronized static void info(String message) {
		log(Level.INFO, message);
	}
	public synchronized static void warning(String message) {
		log(Level.WARNING, message);
	}
	public synchronized static void severe(String message) {
		log(Level.SEVERE, message);
	}
	
	
	
	
	
	
	
	private static void log(Level l, String message) {
		String line = "["+Thread.currentThread().getName()+"] ["+l.name()+"] "+message;
		// messages.add(line);
		if (Server.serverInstance != null) {
			Server.serverInstance.console.println(line);
		}
		else {
			if (l == Level.INFO)
				System.out.println(line);
			else
				System.err.println(line);
		}
		
		try {
			PacketServerLog packet = new PacketServerLog();
			packet.setMessage(line);
			Server.serverInstance.playerManager.sendToAll(packet);
		} catch(Exception e) {  }
	}
	
	
	private static enum Level {
		INFO, WARNING, SEVERE
	}

}
