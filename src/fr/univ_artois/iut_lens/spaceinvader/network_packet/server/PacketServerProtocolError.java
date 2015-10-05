package fr.univ_artois.iut_lens.spaceinvader.network_packet.server;

public class PacketServerProtocolError extends PacketServer {

	public PacketServerProtocolError() {
		super((byte)0x50);
	}
	
	public void setMessage(String message) {
		if (message == null)
			message = "";
		setData(message);
	}
	
	public String getMessage() {
		return new String(getData(), CHARSET);
	}

}
