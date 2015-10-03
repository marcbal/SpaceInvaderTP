package fr.univ_artois.iut_lens.spaceinvader.network_packet.server;

import java.util.Map;

import com.google.gson.Gson;

public class PacketServerLevelEnd extends PacketServer {
	
	public PacketServerLevelEnd() {
		super(118);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getScores() {
		return new Gson().fromJson(getData(), Map.class);
	}
	
	public void setScores(Map<String, Object> data) {
		setData(new Gson().toJson(data));
	}

}
