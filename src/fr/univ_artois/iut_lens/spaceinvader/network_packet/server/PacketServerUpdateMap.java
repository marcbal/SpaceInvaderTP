package fr.univ_artois.iut_lens.spaceinvader.network_packet.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.univ_artois.iut_lens.spaceinvader.sprites_manager.Sprite;

public class PacketServerUpdateMap extends PacketServer {

	public PacketServerUpdateMap() {
		super((byte)0x43);
	}

	public MapData getEntityData() {
		return null; // TODO
	}
	
	public void setEntityData(MapData data) {
		// TODO
	}
	
	
	
	
	public static class MapData {

		public List<EntityDataSpawn> spawningEntities = new ArrayList<EntityDataSpawn>();
		public static class EntityDataSpawn {
			public int id;
			public Sprite sprite;
			public String name;
			public int maxLife = 0;
			
			public double posX;
			public double posY;
			public double speedX;
			public double speedY;
			public int currentLife = 0;
		}

		public List<EntityDataUpdated> updatingEntities = new ArrayList<EntityDataUpdated>();
		public static class EntityDataUpdated {
			public int id;
			public double posX;
			public double posY;
			public double speedX;
			public double speedY;
			public int currentLife = 0;
		}

		public List<Integer> removedEntities = new ArrayList<Integer>();
		
		public Map<Integer, String> spritesData = new HashMap<Integer, String>();
		
	}
	/*
	 * 
	public Map<Integer, String> getSpriteData() {
		Map<Integer, String> ret = new 
		ByteBuffer bb = ByteBuffer.wrap(getData());
		int nbSprite = bb.getInt();
		for (int i=0; i<nbSprite; i++) {
			int id = bb.getInt();
			byte[] path = new byte[bb.getInt()];
			bb.get(path);
			ret.put(id, new String(path, CHARSET));
		}
		return ret;
	}
	
	public void setSpriteData(Map<String, Sprite> data) {
		ByteBuffer bb = ByteBuffer.allocate(4+data.size()*(4+4+100));
		bb.putInt(data.size());
		for (Entry<String, Sprite> sp : data.entrySet()) {
			bb.putInt(sp.getValue().id);
			bb.putInt(sp.getKey().getBytes(CHARSET).length);
			bb.put(sp.getKey().getBytes(CHARSET));
		}
		setData(Arrays.copyOf(bb.array(), bb.position()));
	}
	 * */
	
}
