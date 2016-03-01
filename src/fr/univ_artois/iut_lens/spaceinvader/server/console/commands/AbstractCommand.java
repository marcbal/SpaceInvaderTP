package fr.univ_artois.iut_lens.spaceinvader.server.console.commands;

import fr.univ_artois.iut_lens.spaceinvader.server.Server;

public abstract class AbstractCommand {
	
	protected Server server = Server.serverInstance;
	
	public final String commandName;
	
	
	public AbstractCommand(String cmdName) {
		commandName = cmdName;
	}
	
	
	
	public abstract void execute(String[] args);
	
	
	
	

	
	
	
	/**
	 * <p>Concatène les chaines de caractères passés dans <code>args</code> (avec <code>" "</code> comme séparateur), en ommettant
	 * celles qui se trouvent avant <code>index</code>.<br/>
	 * Par exemple :</p>
	 * <code>
	 * getLastParams(new String[] {"test", "bouya", "chaka", "bukkit"}, 1);
	 * </code>
	 * <p>retournera la chaine "bouya chaka bukkit"
	 * @param args liste des arguments d'une commandes.<br/>
	 * 			Le premier élément est l'argument qui suit le nom de la commande.
	 * 			Usuellement, ce paramètre correspond au paramètre <code>args</code> de la méthode onCommand
	 * @param index 
	 * @return
	 */
	public static String getLastParam(String[] args, int index) {
		if (index < 0 || index >= args.length)
			return null;
		
		String ret = "";
		
		for (int i = index; i<args.length; i++) {
			ret += args[i] + " ";
		}
		
		if (ret.length() > 0)
			ret = ret.substring(0, ret.length()-1);
		return ret;
	}

}
