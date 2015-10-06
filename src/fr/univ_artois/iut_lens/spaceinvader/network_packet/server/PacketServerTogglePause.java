package fr.univ_artois.iut_lens.spaceinvader.network_packet.server;

public class PacketServerTogglePause extends PacketServer {

	public PacketServerTogglePause() {
		super((byte)0x41);
		setData(new byte[] {0});
	}

	
	public void setPause(boolean p) {
		getData()[0] = p ? (byte)0x01 : (byte)0x00;
	}
	
	
	public boolean getPause() {
		return getData()[0] == (byte)0x01;
	}

}
