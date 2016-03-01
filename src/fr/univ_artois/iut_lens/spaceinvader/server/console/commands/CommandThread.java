package fr.univ_artois.iut_lens.spaceinvader.server.console.commands;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;

import fr.univ_artois.iut_lens.spaceinvader.util.Logger;

public class CommandThread extends AbstractCommand {
	
	public CommandThread() {
		super("thread");
	}

	@Override
	public void execute(String[] args) {
		ThreadInfo[] threadsInfo = ManagementFactory.getThreadMXBean().dumpAllThreads(false, false);

		ThreadInfo threadInfoSelected = null;
		
		if (args.length > 1)
		{
			String threadName = getLastParam(args, 1);
			for (ThreadInfo th : threadsInfo)
				if (th != null && th.getThreadName().equalsIgnoreCase(threadName))
					threadInfoSelected = th;
		}
		
		if (threadInfoSelected != null) {
			Logger.info("#"+threadInfoSelected.getThreadId()
					+" "+threadInfoSelected.getThreadName()
					+" : "+threadInfoSelected.getThreadState().toString());
			
			StackTraceElement[] stack = threadInfoSelected.getStackTrace();
			
			Logger.info("StackTrace : ("+stack.length+" element(s))");
			
			for (StackTraceElement stEl : stack) {
				Logger.info(" - "+stEl.toString());
			}
			
		}
		else {
			Logger.warning("Le thread n'a pas été trouvé");
		}
	}

}
