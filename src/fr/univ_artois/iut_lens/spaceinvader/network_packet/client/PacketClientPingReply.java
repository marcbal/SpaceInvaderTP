package fr.univ_artois.iut_lens.spaceinvader.network_packet.client;

import java.nio.ByteBuffer;

public class PacketClientPingReply extends PacketClient {

	public PacketClientPingReply() {
		super((byte)0x0D);
	}
	
	public int getPingId() {
		return ByteBuffer.wrap(getData()).getInt();
	}
	
	public void setPingId(int id) {
		setData(ByteBuffer.allocate(4).putInt(id).array());
	}

}
