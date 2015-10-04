package fr.univ_artois.iut_lens.spaceinvader.client.network;

import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServer;

public interface NetworkReceiveListener {
	
	public void onReceivePacket(PacketServer packet);
	
}
