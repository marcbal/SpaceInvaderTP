package fr.univ_artois.iut_lens.spaceinvader.network_packet.client;

public class PacketClientDisconnect extends PacketClient {

	public PacketClientDisconnect() {
		super((byte)0x0F);
	}

}
