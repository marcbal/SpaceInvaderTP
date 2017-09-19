package fr.univ_artois.iut_lens.spaceinvader.network_packet.server;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketServerLevelEnd extends PacketServer {
	
	public PacketServerLevelEnd() {
		super((byte)0x4E);
	}
	
	public List<PlayerScore> getScores() {
		byte[] data = getData();
		ByteBuffer bb = ByteBuffer.wrap(data);
		int nbScores = bb.getInt();
		List<PlayerScore> scores = new ArrayList<>(nbScores);
		for (int i = 0; i<nbScores; i++) {
			PlayerScore ps = new PlayerScore();
			ps.score = bb.getLong();
			byte[] nameContainer = new byte[bb.getInt()];
			bb.get(nameContainer);
			ps.playerName = new String(nameContainer, CHARSET);
			scores.add(ps);
		}
		return scores;
	}
	
	public void setScores(List<PlayerScore> scores) {
		ByteBuffer bb = ByteBuffer.allocate(scores.size()*(8+4+20)+100);
		bb.putInt(scores.size());
		for (PlayerScore ps : scores) {
			bb.putLong(ps.score);
			bb.putInt(ps.playerName.getBytes(CHARSET).length);
			bb.put(ps.playerName.getBytes(CHARSET));
		}
		setData(Arrays.copyOf(bb.array(), bb.position()));
	}
	
	
	public static class PlayerScore {
		public String playerName;
		public long score;
	}

}
