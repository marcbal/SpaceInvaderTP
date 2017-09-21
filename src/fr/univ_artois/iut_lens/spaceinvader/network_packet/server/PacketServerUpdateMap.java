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

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap.MapData.EntityDataSpawn;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap.MapData.EntityDataUpdated;

public class PacketServerUpdateMap extends PacketServer {
	
	/*
	 * Utilities to convert short values to double values and vice-versa
	 * this packet use short value to send to player the position and speed of entities
	 * It's to use less bytes (short = 2 bytes) instead of 4 for a float.
	 * So we need conversion methods to easily convert values
	 */
	public static final double POS_COEFF = Short.MAX_VALUE / Math.max(MegaSpaceInvader.DISPLAY_WIDTH, MegaSpaceInvader.DISPLAY_HEIGHT);
	public static final double SPEED_COEFF = POS_COEFF / 5; // if we dont divide, very fast entities will slow down to fit into Short limits

	// +- 1 only to avoid number overflow when converting from double and rounding the value before cast to short
	public static final double MIN_X_POS_ALLOWED = (Short.MIN_VALUE+1) / POS_COEFF + (MegaSpaceInvader.DISPLAY_WIDTH / 2d);
	public static final double MAX_X_POS_ALLOWED = (Short.MAX_VALUE-1) / POS_COEFF + (MegaSpaceInvader.DISPLAY_WIDTH / 2d);
	public static final double MIN_Y_POS_ALLOWED = (Short.MIN_VALUE+1) / POS_COEFF + (MegaSpaceInvader.DISPLAY_HEIGHT / 2d);
	public static final double MAX_Y_POS_ALLOWED = (Short.MAX_VALUE-1) / POS_COEFF + (MegaSpaceInvader.DISPLAY_HEIGHT / 2d);
	public static final double MIN_SPEED_ALLOWED = (Short.MIN_VALUE+1) / SPEED_COEFF;
	public static final double MAX_SPEED_ALLOWED = (Short.MAX_VALUE-1) / SPEED_COEFF; // equals to 1 screen/frame

	public static double shortToPosX(short posX) {
		return posX / POS_COEFF + (MegaSpaceInvader.DISPLAY_WIDTH / 2d);
	}
	public static double shortToPosY(short posY) {
		return posY / POS_COEFF + (MegaSpaceInvader.DISPLAY_HEIGHT / 2d);
	}
	public static double shortToSpeed(short speed) {
		return speed / SPEED_COEFF;
	}
	
	public static short speedToShort(double speed) {
		if (speed < MIN_SPEED_ALLOWED) speed = MIN_SPEED_ALLOWED;
		else if (speed > MAX_SPEED_ALLOWED) speed = MAX_SPEED_ALLOWED;
		return (short) Math.round(speed * SPEED_COEFF);
	}
	
	public static short posXToShort(double posX) {
		if (posX < MIN_X_POS_ALLOWED) posX = MIN_X_POS_ALLOWED;
		else if (posX > MAX_X_POS_ALLOWED) posX = MAX_X_POS_ALLOWED;
		return (short) Math.round((posX - (MegaSpaceInvader.DISPLAY_WIDTH / 2d)) * POS_COEFF);
	}
	
	public static short posYToShort(double posY) {
		if (posY < MIN_Y_POS_ALLOWED) posY = MIN_Y_POS_ALLOWED;
		else if (posY > MAX_Y_POS_ALLOWED) posY = MAX_Y_POS_ALLOWED;
		return (short) Math.round((posY - (MegaSpaceInvader.DISPLAY_HEIGHT / 2d)) * POS_COEFF);
	}
	
	
	public static void main(String[] args) {
		System.out.println(shortToPosX(posXToShort(200)));
		System.out.println(shortToPosY(posYToShort(-30)));
		System.out.println(shortToSpeed(speedToShort(3000.1)));
	}
	
	
	
	
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
			ent.posX = shortToPosX(bb.getShort());
			ent.posY = shortToPosY(bb.getShort());
			ent.speedX = shortToSpeed(bb.getShort());
			ent.speedY = shortToSpeed(bb.getShort());
			ent.currentLife = bb.getInt();
			
			data.spawningEntities.add(ent);
		}
		

		int nbUpdatedEntity = bb.getInt();
		for (int i=0; i<nbUpdatedEntity; i++) {
			EntityDataUpdated ent = new EntityDataUpdated();
			ent.id = bb.getInt();
			ent.posX = shortToPosX(bb.getShort());
			ent.posY = shortToPosY(bb.getShort());
			ent.speedX = shortToSpeed(bb.getShort());
			ent.speedY = shortToSpeed(bb.getShort());
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
				4+data.spawningEntities.size()*(4+4+4+50+4+2+2+2+2+4)
				+4+data.updatingEntities.size()*(4+2+2+2+2+4)
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
			bb.putShort(posXToShort(ent.posX));
			bb.putShort(posYToShort(ent.posY));
			bb.putShort(speedToShort(ent.speedX));
			bb.putShort(speedToShort(ent.speedY));
			bb.putInt(ent.currentLife);
		}
		

		bb.putInt(data.updatingEntities.size());
		for (EntityDataUpdated ent : data.updatingEntities) {
			bb.putInt(ent.id);
			bb.putShort(posXToShort(ent.posX));
			bb.putShort(posYToShort(ent.posY));
			bb.putShort(speedToShort(ent.speedX));
			bb.putShort(speedToShort(ent.speedY));
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
			
			public double posX;
			public double posY;
			public double speedX;
			public double speedY;
			public int currentLife = 0;
		}

		public List<EntityDataUpdated> updatingEntities = new ArrayList<>();
		public static class EntityDataUpdated {
			public int id;
			public double posX;
			public double posY;
			public double speedX;
			public double speedY;
			public int currentLife = 0;
		}

		public List<Integer> removedEntities = new ArrayList<>();
		
		public Map<Integer, String> spritesData = new HashMap<>();
		
	}
	
}
