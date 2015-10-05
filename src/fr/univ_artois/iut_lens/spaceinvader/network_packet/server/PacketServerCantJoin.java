package fr.univ_artois.iut_lens.spaceinvader.network_packet.server;

public class PacketServerCantJoin extends PacketServer {

	public PacketServerCantJoin() {
		super((byte)0x60);
	}
	
	public void setReason(String r) {
		if (r == null) r = "";
		setData(r);
	}
	
	public String getReason() {
		return new String(getData(), CHARSET);
	}

}
