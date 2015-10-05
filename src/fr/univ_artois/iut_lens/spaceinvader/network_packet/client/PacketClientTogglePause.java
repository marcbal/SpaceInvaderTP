package fr.univ_artois.iut_lens.spaceinvader.network_packet.client;

public class PacketClientTogglePause extends PacketClient {
	
	public PacketClientTogglePause() {
		super((byte)0x02);
		setData(new byte[] {0x00});
	}
	
	public void setPause(boolean p) {
		getData()[0] = p ? (byte)0x01 : (byte)0x00;
	}
	
	
	public boolean getPause() {
		return getData()[0] == (byte)0x01;
	}
	
}
