package fr.univ_artois.iut_lens.spaceinvader.server.network;

import java.net.SocketAddress;

public interface NetworkReceiveListener {
	
	public void onPlayerSendName(SocketAddress playerAddr, String name);
	
	
	
	
	public void onPlayerSendPlayCoordinate(SocketAddress playerAddr, int ligne, int colonne);
	
	
	
}
