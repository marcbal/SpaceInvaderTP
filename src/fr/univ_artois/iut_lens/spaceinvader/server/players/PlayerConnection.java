package fr.univ_artois.iut_lens.spaceinvader.server.players;

import java.io.IOException;
import java.net.SocketAddress;

import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServer;
import fr.univ_artois.iut_lens.spaceinvader.server.network.ServerConnection;

public class PlayerConnection {
	
	private ServerConnection connection;
	
	private SocketAddress remoteAddr;
	
	public PlayerConnection(SocketAddress addr, ServerConnection co) {
		remoteAddr = addr;
		connection = co;
	}
	
	public SocketAddress getRemoteAddr() {
		return remoteAddr;
	}
	
	public boolean send(PacketServer p) {
		try {
			connection.send(remoteAddr, p);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
	
}
