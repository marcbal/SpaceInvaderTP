package fr.univ_artois.iut_lens.spaceinvader.network_packet.server;

import java.util.Map;

import com.google.gson.Gson;

public class PacketServerUpdateMap extends PacketServer {

	public PacketServerUpdateMap() {
		super(113);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getEntityData() {
		return new Gson().fromJson(getData(), Map.class);
	}
	
	public void setEntityData(Map<String, Object> data) {
		setData(new Gson().toJson(data));
	}

}
