package fr.univ_artois.iut_lens.spaceinvader.server.network;

import java.net.SocketAddress;

import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClient;

public interface NetworkReceiveListener {
	
	public void onReceivePacket(SocketAddress playerAddr, PacketClient packet);
	
}
