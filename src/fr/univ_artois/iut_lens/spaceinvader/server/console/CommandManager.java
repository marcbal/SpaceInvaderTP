package fr.univ_artois.iut_lens.spaceinvader.server.console;

import java.util.HashMap;
import java.util.Map;

import fr.univ_artois.iut_lens.spaceinvader.server.console.commands.AbstractCommand;
import fr.univ_artois.iut_lens.spaceinvader.server.console.commands.CommandStop;
import fr.univ_artois.iut_lens.spaceinvader.server.console.commands.CommandThreads;

public class CommandManager implements CommandManagerInterface {
	
	private Map<String, AbstractCommand> commands = new HashMap<>();
	
	
	public CommandManager() {
		addCommand(new CommandStop());
		addCommand(new CommandThreads());
	}
	
	
	
	
	
	public void addCommand(AbstractCommand cmd) {
		commands.put(cmd.commandName.toLowerCase(), cmd);
	}
	

	@Override
	public void dispatchCommand(String cmd, String[] args) {
		try {
			if (commands.containsKey(cmd.toLowerCase()))
				commands.get(cmd.toLowerCase()).execute(args);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
