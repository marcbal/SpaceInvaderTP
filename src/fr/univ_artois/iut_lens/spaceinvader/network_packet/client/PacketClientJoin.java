package fr.univ_artois.iut_lens.spaceinvader.network_packet.client;

public class PacketClientJoin extends PacketClient {
	
	public PacketClientJoin() {
		super(210);
	}
	
	
	public void setPlayerName(String name) {
		if (name == null) name = "";
		setData(name);
	}
	
	public String getPlayerName() {
		return getData();
	}

}
