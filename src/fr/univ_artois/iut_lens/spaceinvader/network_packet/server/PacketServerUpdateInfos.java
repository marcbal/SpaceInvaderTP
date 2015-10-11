package fr.univ_artois.iut_lens.spaceinvader.network_packet.server;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateInfos.GameInfo.PlayerInfo;

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
		data.currentShip = bb.getInt();
		data.nbShip = bb.getInt();
		data.maxTPS = bb.getInt();
		data.currentTickTime = bb.getLong();
		
		
		int playerDataSize = bb.getInt();
		for (int i=0; i<playerDataSize; i++) {
			PlayerInfo pInfo = new PlayerInfo();

			byte[] name = new byte[bb.getInt()];
			bb.get(name);
			pInfo.name = new String(name, CHARSET);
			
			pInfo.score = bb.getLong();
			pInfo.ping = bb.getInt();
			pInfo.upBandwidth = bb.getLong();
			pInfo.downBandwidth = bb.getLong();
			data.playerInfos.add(pInfo);
		}
		
		return data;
	}
	
	
	
	public void setInfos(GameInfo data) {
		ByteBuffer bb = ByteBuffer.allocate(4+4+8+8+4+4+4+4+4+8+4
				+data.playerInfos.size()*(4+50+8+4+8+8)
				+100);
		bb.putInt(data.nbEntity);
		bb.putInt(data.nbCollisionThreads);
		bb.putLong(data.currentEnemyLife);
		bb.putLong(data.maxEnemyLife);
		bb.putInt(data.currentLevel);
		bb.putInt(data.nbLevel);
		bb.putInt(data.currentShip);
		bb.putInt(data.nbShip);
		bb.putInt(data.maxTPS);
		bb.putLong(data.currentTickTime);
		
		bb.putInt(data.playerInfos.size());
		for (PlayerInfo pInfo : data.playerInfos) {
			bb.putInt(pInfo.name.getBytes(CHARSET).length);
			bb.put(pInfo.name.getBytes(CHARSET));
			bb.putLong(pInfo.score);
			bb.putInt(pInfo.ping);
			bb.putLong(pInfo.upBandwidth);
			bb.putLong(pInfo.downBandwidth);
		}

		setData(Arrays.copyOf(bb.array(), bb.position()));
	}
	
	
	
	
	public static class GameInfo {
		public int nbEntity;
		public int nbCollisionThreads;
		public long currentEnemyLife;
		public long maxEnemyLife;
		public int currentLevel;
		public int nbLevel;
		public int currentShip;
		public int nbShip;
		public int maxTPS = 1;
		public long currentTickTime = 1;
		public List<PlayerInfo> playerInfos = new ArrayList<PlayerInfo>();
		
		public static class PlayerInfo {
			public String name;
			public long score;
			/** En millisecondes. */
			public int ping;
			/** Bande passante montante côté serveur. */
			public long upBandwidth;
			/** Bande passante descendante côté serveur. */
			public long downBandwidth;
		}
	}
	
	

}
