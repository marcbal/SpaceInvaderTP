package fr.univ_artois.iut_lens.spaceinvader.server.console.commands;

import fr.univ_artois.iut_lens.spaceinvader.client.Client;
import fr.univ_artois.iut_lens.spaceinvader.server.Server;
import fr.univ_artois.iut_lens.spaceinvader.util.Logger;

public class CommandStop extends AbstractCommand {

	public CommandStop() {
		super("stop");
	}

	@Override
	public void execute(String[] args) {
		Logger.info("Stopping process ...");
		if (Client.instance != null)
			Client.instance.gameRunning.set(false);
		if (Server.serverInstance != null)
			Server.serverInstance.gameRunning.set(false);
		try { Thread.sleep(5000); } catch (InterruptedException e) { }
		System.exit(0);
	}

}
