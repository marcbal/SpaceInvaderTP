package fr.univ_artois.iut_lens.spaceinvader.network_packet;

import java.util.HashMap;
import java.util.Map;

import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientCommand;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientDisconnect;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientJoin;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientNextLevel;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientTogglePause;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerCantJoin;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerConnectionOk;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerDisconnectOk;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerDisconnectTimeout;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerLevelEnd;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerLevelStart;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerProtocolError;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerSpritesData;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateInfos;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap;
import fr.univ_artois.iut_lens.spaceinvader.server.network.ServerConnection.InvalidClientMessage;

public abstract class Packet {
	
	private final int code;
	private String data = "";
	
	public Packet(int c) {
		code = c;
	}

	public int getCode() { return code; }

	public String getData() { return data; }

	protected void setData(String d) { data = d; }
	
	
	
	
	
	
	
	
	
	private static Map<Integer, Class<? extends Packet>> packetTypes = new HashMap<Integer, Class<? extends Packet>>();
	
	public static Packet constructPacket(int code, String data) {
		if (!packetTypes.containsKey(code))
			throw new InvalidClientMessage("le code du packet ne correspond à aucun type de packet : "+code);
		
		try {
			Packet p = packetTypes.get(code).newInstance();
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
		addPacket(PacketClientJoin.class);
		addPacket(PacketClientCommand.class);
		addPacket(PacketClientTogglePause.class);
		addPacket(PacketClientNextLevel.class);
		addPacket(PacketClientDisconnect.class);
		
		
		// packets envoyés par le serveur
		addPacket(PacketServerConnectionOk.class);
		addPacket(PacketServerProtocolError.class);
		addPacket(PacketServerCantJoin.class);
		addPacket(PacketServerSpritesData.class);
		addPacket(PacketServerLevelStart.class);
		addPacket(PacketServerUpdateMap.class);
		addPacket(PacketServerUpdateInfos.class);
		addPacket(PacketServerLevelEnd.class);
		addPacket(PacketServerDisconnectOk.class);
		addPacket(PacketServerDisconnectTimeout.class);
	}
	

}
