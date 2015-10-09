package fr.univ_artois.iut_lens.spaceinvader.network_packet.server;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap.MapData.EntityDataSpawn;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap.MapData.EntityDataUpdated;

public class PacketServerUpdateMap extends PacketServer {

	public PacketServerUpdateMap() {
		super((byte)0x43);
	}

	public MapData getEntityData() {
		if (getData().length == 0)
			return null;
		MapData data = new MapData();
		ByteBuffer bb = ByteBuffer.wrap(getData());
		

		int nbSpawningEntity = bb.getInt();
		for (int i=0; i<nbSpawningEntity; i++) {
			EntityDataSpawn ent = new EntityDataSpawn();
			ent.id = bb.getInt();
			ent.spriteId = bb.getInt();
			byte[] name = new byte[bb.getInt()];
			bb.get(name);
			ent.name = new String(name, CHARSET);
			ent.maxLife = bb.getInt();
			ent.posX = bb.getDouble();
			ent.posY = bb.getDouble();
			ent.speedX = bb.getDouble();
			ent.speedY = bb.getDouble();
			ent.currentLife = bb.getInt();
			
			data.spawningEntities.add(ent);
		}
		

		int nbUpdatedEntity = bb.getInt();
		for (int i=0; i<nbUpdatedEntity; i++) {
			EntityDataUpdated ent = new EntityDataUpdated();
			ent.id = bb.getInt();
			ent.posX = bb.getDouble();
			ent.posY = bb.getDouble();
			ent.speedX = bb.getDouble();
			ent.speedY = bb.getDouble();
			ent.currentLife = bb.getInt();
			
			data.updatingEntities.add(ent);
		}

		int nbRemovedEntity = bb.getInt();
		for (int i=0; i<nbRemovedEntity; i++) {
			data.removedEntities.add(bb.getInt());
		}

		int nbSprites = bb.getInt();
		for (int i=0; i<nbSprites; i++) {
			int k = bb.getInt();
			byte[] vb = new byte[bb.getInt()];
			bb.get(vb);
			data.spritesData.put(k, new String(vb, CHARSET));
		}

		
		return data;
	}
	
	public void setEntityData(MapData data) {
		ByteBuffer bb = ByteBuffer.allocate(
				4+data.spawningEntities.size()*(4+4+4+50+4+8+8+8+8+4)
				+4+data.updatingEntities.size()*(4+8+8+8+8+4)
				+4+data.removedEntities.size()*4
				+4+data.spritesData.size()*(4+4+100)
				+1000);
		
		bb.putInt(data.spawningEntities.size());
		for (EntityDataSpawn ent : data.spawningEntities) {
			bb.putInt(ent.id);
			bb.putInt(ent.spriteId);
			bb.putInt(ent.name.getBytes(CHARSET).length);
			bb.put(ent.name.getBytes(CHARSET));
			bb.putInt(ent.maxLife);
			bb.putDouble(ent.posX);
			bb.putDouble(ent.posY);
			bb.putDouble(ent.speedX);
			bb.putDouble(ent.speedY);
			bb.putInt(ent.currentLife);
		}
		

		bb.putInt(data.updatingEntities.size());
		for (EntityDataUpdated ent : data.updatingEntities) {
			bb.putInt(ent.id);
			bb.putDouble(ent.posX);
			bb.putDouble(ent.posY);
			bb.putDouble(ent.speedX);
			bb.putDouble(ent.speedY);
			bb.putInt(ent.currentLife);
		}

		bb.putInt(data.removedEntities.size());
		for (int id : data.removedEntities) {
			bb.putInt(id);
		}
		
		bb.putInt(data.spritesData.size());
		for (Entry<Integer, String> spr : data.spritesData.entrySet()) {
			bb.putInt(spr.getKey());
			bb.putInt(spr.getValue().getBytes(CHARSET).length);
			bb.put(spr.getValue().getBytes(CHARSET));
		}

		setData(Arrays.copyOf(bb.array(), bb.position()));
		
	}
	
	
	
	
	public static class MapData {

		public List<EntityDataSpawn> spawningEntities = new ArrayList<EntityDataSpawn>();
		public static class EntityDataSpawn {
			public int id;
			public int spriteId;
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
