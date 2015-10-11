package fr.univ_artois.iut_lens.spaceinvader.server.network;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fr.univ_artois.iut_lens.spaceinvader.server.network.ServerConnection.InputConnectionThread;

public class BandwidthCalculation {
	
	private List<PacketStat> packetHistory = new LinkedList<PacketStat>();
	
	
	public synchronized void addPacket(InputConnectionThread co, boolean in, long size) {
		packetHistory.add(new PacketStat(co, in, size));
	}
	
	
	public synchronized long getBandWidth(Boolean input, InputConnectionThread co) {
		long currentTime = System.currentTimeMillis();
		Iterator<PacketStat> it = packetHistory.iterator();
		long sum = 0;
		while(it.hasNext()) {
			PacketStat el = it.next();
			if (el.time < currentTime - 1000) {
				it.remove();
				continue;
			}
			if (input != null && el.input != input.booleanValue())
				continue;
			if (co != null && !co.equals(el.connection))
				continue;
			sum += el.packetSize;
		}
		return sum;
	}
	
	
	
	
	
	
	
	
	
	
	
	private class PacketStat {
		public final long time;
		public final long packetSize;
		public final boolean input;
		public final InputConnectionThread connection;
		public PacketStat(InputConnectionThread co, boolean input, long size) {
			time = System.currentTimeMillis();
			packetSize = size;
			this.input = input;
			connection = co;
		}
	}

}
