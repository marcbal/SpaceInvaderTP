package fr.univ_artois.iut_lens.spaceinvader.network_packet.server;

import java.util.Map;

import com.google.gson.Gson;

public class PacketServerUpdateInfos extends PacketServer {

	public PacketServerUpdateInfos() {
		super(114);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getInfos() {
		return new Gson().fromJson(getData(), Map.class);
	}
	
	public void setInfos(Map<String, Object> data) {
		setData(new Gson().toJson(data));
	}

}
