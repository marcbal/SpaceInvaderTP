package fr.univ_artois.iut_lens.spaceinvader.server.network;

import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClient;
import fr.univ_artois.iut_lens.spaceinvader.server.network.ServerConnection.ConnectionThread;

public interface NetworkReceiveListener {
	
	public void onReceivePacket(ConnectionThread co, PacketClient packet);
	
}
