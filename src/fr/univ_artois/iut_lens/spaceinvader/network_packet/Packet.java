package fr.univ_artois.iut_lens.spaceinvader.network_packet;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientCommand;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientDisconnect;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientJoin;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientNextLevel;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientPong;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientTogglePause;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerCantJoin;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerConnectionOk;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerDisconnectOk;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerDisconnectTimeout;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerLevelEnd;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerLevelStart;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerPing;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerProtocolError;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerTogglePause;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateInfos;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap;
import fr.univ_artois.iut_lens.spaceinvader.server.network.ServerConnection.InvalidClientMessage;

public abstract class Packet {
	
	private final byte code;
	private byte[] data = new byte[0];
	
	public Packet(byte c) {
		code = c;
	}

	public byte getCode() { return code; }

	public byte[] constructAndGetDataPacket() {
		return ByteBuffer.allocate(1+data.length+4).put(code).putInt(data.length).put(data).array();
	}

	protected void setData(byte[] d) { data = d; }
	
	protected void setData(String d) { data = d.getBytes(CHARSET); }
	
	protected byte[] getData() { return data; }
	
	
	
	
	
	
	
	public static final Charset CHARSET = MegaSpaceInvader.NETWORK_CHARSET;
	
	private static Map<Byte, Class<? extends Packet>> packetTypes = new HashMap<Byte, Class<? extends Packet>>();
	
	public static Packet constructPacket(byte[] data) {
		if (!packetTypes.containsKey(data[0]))
			throw new InvalidClientMessage("le code du packet ne correspond à aucun type de packet : "+data[0]);
		
		try {
			Packet p = packetTypes.get(data[0]).newInstance();
			p.setData(Arrays.copyOfRange(data, 5, data.length));
			return p;
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidClientMessage("erreur lors de la construction du packet");
		}
	}
	
	private static <T extends Packet> void addPacket(Class<T> packetClass) {
		try {
			Packet p = (Packet)packetClass.newInstance();
			packetTypes.put(p.code, packetClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	static {
		
		/*
		 * Ajout des types de packets
		 */
		
		// packets envoyés par le client
		addPacket(PacketClientCommand.class);
		addPacket(PacketClientDisconnect.class);
		addPacket(PacketClientJoin.class);
		addPacket(PacketClientNextLevel.class);
		addPacket(PacketClientPong.class);
		addPacket(PacketClientTogglePause.class);
		
		
		// packets envoyés par le serveur
		addPacket(PacketServerCantJoin.class);
		addPacket(PacketServerConnectionOk.class);
		addPacket(PacketServerDisconnectOk.class);
		addPacket(PacketServerDisconnectTimeout.class);
		addPacket(PacketServerLevelEnd.class);
		addPacket(PacketServerLevelStart.class);
		addPacket(PacketServerPing.class);
		addPacket(PacketServerProtocolError.class);
		addPacket(PacketServerTogglePause.class);
		addPacket(PacketServerUpdateInfos.class);
		addPacket(PacketServerUpdateMap.class);
	}
	

}
