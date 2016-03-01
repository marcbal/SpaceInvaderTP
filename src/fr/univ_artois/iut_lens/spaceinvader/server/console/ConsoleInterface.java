package fr.univ_artois.iut_lens.spaceinvader.server.console;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import jline.console.ConsoleReader;

public class ConsoleInterface {
	
	
	public static final String ANSI_RESET = "\u001B[0m";
	
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_DARK_RED = "\u001B[31m";
	public static final String ANSI_DARK_GREEN = "\u001B[32m";
	public static final String ANSI_GOLD = "\u001B[33m";
	public static final String ANSI_DARK_BLUE = "\u001B[34m";
	public static final String ANSI_DARK_PURPLE = "\u001B[35m";
	public static final String ANSI_DARK_AQUA = "\u001B[36m";
	public static final String ANSI_GRAY = "\u001B[37m";
	
	public static final String ANSI_DARK_GRAY = "\u001B[30;1m";
	public static final String ANSI_RED = "\u001B[31;1m";
	public static final String ANSI_GREEN = "\u001B[32;1m";
	public static final String ANSI_YELLOW = "\u001B[33;1m";
	public static final String ANSI_BLUE = "\u001B[34;1m";
	public static final String ANSI_LIGHT_PURPLE = "\u001B[35;1m";
	public static final String ANSI_AQUA = "\u001B[36;1m";
	public static final String ANSI_WHITE = "\u001B[37;1m";
	
	public static final String ANSI_BOLD = "\u001B[1m";
	
	public static final String ANSI_CLEAR_SCREEN = "\u001B[2J\u001B[1;1H";
	
	
	
	private ConsoleReader reader;
	private PrintWriter out;
	
	
	private CommandManagerInterface commandManager;
	
	
	public ConsoleInterface(CommandManagerInterface cmdMan) throws IOException {
		reader = new ConsoleReader();
		reader.setBellEnabled(false);
		reader.setPrompt("\r"+ANSI_LIGHT_PURPLE+">"+ANSI_RESET);
		out = new PrintWriter(reader.getOutput());
		
		commandManager = cmdMan;
		
	}
	
	
	
	
	
	public void loop() {
		
		String line;
		try {
			while((line = reader.readLine()) != null) {
				if (line.trim().equals(""))
					continue;
				String[] args = line.trim().split(" ");
				String command = args[0];
				args = Arrays.copyOfRange(args, 1, args.length);
				
				commandManager.dispatchCommand(command, args);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	public synchronized void println(String str) {
		try {
			out.println('\r'+ANSI_RESET+str);
			out.flush();
			reader.drawLine();
			reader.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
