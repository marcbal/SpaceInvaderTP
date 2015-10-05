package fr.univ_artois.iut_lens.spaceinvader.network_packet.server;

public class PacketServerUpdateInfos extends PacketServer {

	public PacketServerUpdateInfos() {
		super((byte)0x44);
	}

	public GameInfo getInfos() {
		return null; // TODO
	}
	
	public void setInfos(GameInfo data) {
		// TODO
	}
	
	
	
	
	public static class GameInfo {
		public int nbEntity;
		public int nbCollisionThreads;
		public int currentEnemyLife;
		public int maxEnemyLife;
		
	}
	
	

}
