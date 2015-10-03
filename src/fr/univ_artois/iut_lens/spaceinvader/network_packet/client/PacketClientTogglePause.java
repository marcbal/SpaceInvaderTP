package fr.univ_artois.iut_lens.spaceinvader.network_packet.client;

public class PacketClientTogglePause extends PacketClient {
	
	public PacketClientTogglePause() {
		super(212);
		setData("0");
	}
	
	public void setPause(boolean p) {
		char[] data = getData().toCharArray();
		data[0] = p ? '1' : '0';
		setData(new String(data));
	}
	
	
	public boolean getPause() {
		return getData().charAt(0) == '1';
	}
	
}
