package fr.univ_artois.iut_lens.spaceinvader.launcher;

import java.util.Arrays;
import java.util.List;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.launcher.LauncherDialog.LaunchingConfiguration;

public class CommandArgsParser {
	
	
	
	
	
	public static LaunchingConfiguration getConfigurationFromArgs(String[] args) {
		
		if (args.length == 0) return null;
		
		for (int i=0;i<args.length; i++)
			args[i] = args[i].toLowerCase();
		
		List<String> argList = Arrays.asList(args);
		
		LaunchingConfiguration config = new LaunchingConfiguration();
		
		int serverConfigPos = argList.indexOf("-s");
		int clientConfigPos = argList.indexOf("-c");
		
		config.serverEnabled = serverConfigPos != -1;
		if (config.serverEnabled) {
			int nbArgServer;
			if (serverConfigPos < clientConfigPos)
				nbArgServer = clientConfigPos-serverConfigPos-1;
			else
				nbArgServer = argList.size() -serverConfigPos-1;
			
			int port = MegaSpaceInvader.SERVER_DEFAULT_PORT;
			if (nbArgServer > 0) {
				try {
					port = Integer.parseInt(argList.get(serverConfigPos+1));
				} catch (NumberFormatException e) {

					throw new IllegalArgumentException("The port number must be an integer.");
				}
			}
			config.serverPort = port;
			config.serverScoring = (nbArgServer > 1 && argList.get(serverConfigPos+2).equals("score"));
		}

		config.clientEnabled = clientConfigPos != -1;
		if (config.clientEnabled) {
			int nbArgClient;
			if (clientConfigPos < serverConfigPos)
				nbArgClient = serverConfigPos-clientConfigPos-1;
			else
				nbArgClient = argList.size() -clientConfigPos-1;
			
			String playerName = LauncherDialog.randomString(3, LauncherDialog.LETTERS)+LauncherDialog.randomString(2, LauncherDialog.NUMBERS);
			if (nbArgClient > 0)
				playerName = argList.get(clientConfigPos+1);
			config.clientPlayerName = playerName;
			
			String serverAddr;
			if (nbArgClient > 1 && !config.serverEnabled)
				serverAddr = argList.get(clientConfigPos+2);
			else if (config.serverEnabled)
				serverAddr = "localhost:"+config.serverPort;
			else
				throw new IllegalArgumentException("A server address must be precised on the client side, as the second client argument");
			config.clientConnectionAddress = serverAddr;
			
		}
		return config;
		
	}
	
	
	

}
