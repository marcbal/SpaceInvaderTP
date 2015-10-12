package fr.univ_artois.iut_lens.spaceinvader.network_packet.server;

import java.nio.ByteBuffer;

public class PacketServerPing extends PacketServer {
	
	public PacketServerPing() {
		super((byte)0x4D);
	}

	public int getPingId() {
		return ByteBuffer.wrap(getData()).getInt();
	}
	
	public void setPingId(int id) {
		setData(ByteBuffer.allocate(4).putInt(id).array());
	}
	
	private static int pingCounter = 0;
	
	public static PacketServerPing generateNewPing() {
		PacketServerPing ping = new PacketServerPing();
		ping.setPingId(pingCounter++);
		return ping;
	}
}
