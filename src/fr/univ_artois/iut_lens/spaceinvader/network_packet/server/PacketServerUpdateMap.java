package fr.univ_artois.iut_lens.spaceinvader.network_packet.server;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap.MapData.EntityDataSpawn;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap.MapData.EntityDataUpdated;

public class PacketServerUpdateMap extends PacketServer {

	public PacketServerUpdateMap() {
		super((byte)0x43);
	}

	public MapData getEntityData() {
		if (getData().length == 0)
			return null;
		
		// uncompress all data
		int uncompressedSize = ByteBuffer.wrap(getData()).getInt(0);
		Inflater inflater = new Inflater();
		inflater.setInput(getData(), 4, getData().length - 4);
		byte[] uncompressedData = new byte[uncompressedSize];
		int uncompressionPos = 0, ret;
		try {
			while((ret = inflater.inflate(uncompressedData, uncompressionPos, uncompressedSize - uncompressionPos)) != 0)
				uncompressionPos += ret;
		} catch (DataFormatException e) {
			throw new RuntimeException("Error while uncompressing packet", e);
		}
		if (uncompressionPos != uncompressedSize) {
			throw new RuntimeException("Uncompressed data size ("+uncompressionPos+") does not correspond to the expected size ("+uncompressedSize+")");
		}
		
		
		
		ByteBuffer bb = ByteBuffer.wrap(uncompressedData);
		MapData data = new MapData();
		int nbSpawningEntity = bb.getInt();
		for (int i=0; i<nbSpawningEntity; i++) {
			EntityDataSpawn ent = new EntityDataSpawn();
			ent.id = bb.getInt();
			ent.spriteId = bb.getInt();
			byte[] name = new byte[bb.getInt()];
			bb.get(name);
			ent.name = new String(name, CHARSET);
			ent.maxLife = bb.getInt();
			ent.posX = bb.getFloat();
			ent.posY = bb.getFloat();
			ent.speedX = bb.getFloat();
			ent.speedY = bb.getFloat();
			ent.currentLife = bb.getInt();
			
			data.spawningEntities.add(ent);
		}
		

		int nbUpdatedEntity = bb.getInt();
		for (int i=0; i<nbUpdatedEntity; i++) {
			EntityDataUpdated ent = new EntityDataUpdated();
			ent.id = bb.getInt();
			ent.posX = bb.getFloat();
			ent.posY = bb.getFloat();
			ent.speedX = bb.getFloat();
			ent.speedY = bb.getFloat();
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
		if (data == null) {
			setData(new byte[0]);
			return;
		}
		
		ByteBuffer bb = ByteBuffer.allocate(
				4+data.spawningEntities.size()*(4+4+4+50+4+4+4+4+4+4)
				+4+data.updatingEntities.size()*(4+4+4+4+4+4)
				+4+data.removedEntities.size()*4
				+4+data.spritesData.size()*(4+4+100)
				+2000);
		
		bb.putInt(data.spawningEntities.size());
		for (EntityDataSpawn ent : data.spawningEntities) {
			bb.putInt(ent.id);
			bb.putInt(ent.spriteId);
			bb.putInt(ent.name.getBytes(CHARSET).length);
			bb.put(ent.name.getBytes(CHARSET));
			bb.putInt(ent.maxLife);
			bb.putFloat(ent.posX);
			bb.putFloat(ent.posY);
			bb.putFloat(ent.speedX);
			bb.putFloat(ent.speedY);
			bb.putInt(ent.currentLife);
		}
		

		bb.putInt(data.updatingEntities.size());
		for (EntityDataUpdated ent : data.updatingEntities) {
			bb.putInt(ent.id);
			bb.putFloat(ent.posX);
			bb.putFloat(ent.posY);
			bb.putFloat(ent.speedX);
			bb.putFloat(ent.speedY);
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
		
		
		
		// compress data
		Deflater def = new Deflater(6);
		def.setInput(Arrays.copyOf(bb.array(), bb.position()));
		int uncompressedSize = bb.position();
		def.finish();
		int compSize = 0, ret;
		// recycling byte buffer, but starting at byte 4
		while ((ret = def.deflate(bb.array(), compSize + 4, bb.array().length - 4 - compSize, Deflater.FULL_FLUSH)) != 0)
			compSize += ret;
		
		// write size of uncompressed data in the byte buffer
		bb.putInt(0, uncompressedSize);
		
		setData(Arrays.copyOf(bb.array(), compSize + 4));
	}
	
	
	
	
	public static class MapData {

		public List<EntityDataSpawn> spawningEntities = new ArrayList<>();
		public static class EntityDataSpawn {
			public int id;
			public int spriteId;
			public String name;
			public int maxLife = 0;
			
			public float posX;
			public float posY;
			public float speedX;
			public float speedY;
			public int currentLife = 0;
		}

		public List<EntityDataUpdated> updatingEntities = new ArrayList<>();
		public static class EntityDataUpdated {
			public int id;
			public float posX;
			public float posY;
			public float speedX;
			public float speedY;
			public int currentLife = 0;
		}

		public List<Integer> removedEntities = new ArrayList<>();
		
		public Map<Integer, String> spritesData = new HashMap<>();
		
	}
	
}
