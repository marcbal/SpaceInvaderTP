package fr.univ_artois.iut_lens.spaceinvader.network_packet.server;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class PacketServerUpdateInfos extends PacketServer {

	public PacketServerUpdateInfos() {
		super((byte)0x44);
	}

	public GameInfo getInfos() {
		if (getData().length == 0)
			return null;
		GameInfo data = new GameInfo();
		ByteBuffer bb = ByteBuffer.wrap(getData());
		data.nbEntity = bb.getInt();
		data.nbCollisionThreads = bb.getInt();
		data.currentEnemyLife = bb.getLong();
		data.maxEnemyLife = bb.getLong();
		data.currentLevel = bb.getInt();
		data.nbLevel = bb.getInt();
		return data;
	}
	
	public void setInfos(GameInfo data) {
		ByteBuffer bb = ByteBuffer.allocate(4+4+8+8+4+4);
		bb.putInt(data.nbEntity);
		bb.putInt(data.nbCollisionThreads);
		bb.putLong(data.currentEnemyLife);
		bb.putLong(data.maxEnemyLife);
		bb.putInt(data.currentLevel);
		bb.putInt(data.nbLevel);

		setData(Arrays.copyOf(bb.array(), bb.position()));
	}
	
	
	
	
	public static class GameInfo {
		public int nbEntity;
		public int nbCollisionThreads;
		public long currentEnemyLife;
		public long maxEnemyLife;
		public int currentLevel;
		public int nbLevel;
	}
	
	

}
