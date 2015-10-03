package fr.univ_artois.iut_lens.spaceinvader.network_packet.server;

import java.util.Map;

import com.google.gson.Gson;

public class PacketServerSpritesData extends PacketServer {

	public PacketServerSpritesData() {
		super(111);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSpriteData() {
		return new Gson().fromJson(getData(), Map.class);
	}
	
	public void setSpriteData(Map<String, Object> data) {
		setData(new Gson().toJson(data));
	}
	
}
