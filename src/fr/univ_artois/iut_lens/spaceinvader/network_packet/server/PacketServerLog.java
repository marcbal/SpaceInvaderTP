package fr.univ_artois.iut_lens.spaceinvader.network_packet.server;

public class PacketServerLog extends PacketServer {

	public PacketServerLog() {
		super((byte)0x4C);
	}
	

	
	
	public void setMessage(String message) {
		if (message == null) message = "";
		setData(message);
	}
	
	public String getMessage() {
		return new String(getData(), CHARSET);
	}

}
