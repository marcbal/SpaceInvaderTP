package fr.univ_artois.iut_lens.spaceinvader.client;

import java.awt.Graphics;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap.MapData;

public class EntityRepresenterManager {
	
	
	
	private Map<Integer, EntityRepresenter> entities = Collections.synchronizedMap(new HashMap<Integer, EntityRepresenter>());
	
	public synchronized void getUpdateFromServer(MapData data) {
		// TODO à développer
	}
	
	
	public synchronized void drawAll(Graphics g) {
		for (EntityRepresenter ent : entities.values()) {
			ent.draw(g);
		}
	}
	
}
