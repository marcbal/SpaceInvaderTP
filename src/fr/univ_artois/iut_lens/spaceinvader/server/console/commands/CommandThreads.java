package fr.univ_artois.iut_lens.spaceinvader.server.console.commands;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;

import fr.univ_artois.iut_lens.spaceinvader.util.Logger;

public class CommandThreads extends AbstractCommand {

	public CommandThreads() {
		super("threads");
	}
	
	@Override
	public void execute(String[] args) {
		ThreadInfo[] threadsInfo = ManagementFactory.getThreadMXBean().dumpAllThreads(false, false);
		
		
		
		int count = 0;
		for(ThreadInfo thrd : threadsInfo)
		{
			if (thrd == null)
				continue;
			count++;
			
			Logger.info("#"+thrd.getThreadId()
					+" "+thrd.getThreadName()+" : "
					+thrd.getThreadState().toString());
			
		}
		
		Logger.info("Total : "+count+" thread"+((count>1)?"s":""));
	}

}
