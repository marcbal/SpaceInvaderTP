package fr.univ_artois.iut_lens.spaceinvader.client.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.Packet;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClient;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientJoin;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServer;
import fr.univ_artois.iut_lens.spaceinvader.util.Logger;

public class Connection {
	
	private DatagramSocket socket;
	private SocketAddress addr;
	private Thread receiverThread;
	private NetworkReceiveListener listener;
	
	
	
	
	public Connection(SocketAddress a, String name, NetworkReceiveListener l) throws IOException {
		if (a == null || name == null || l == null)
			throw new IllegalArgumentException("les arguments ne peuvent pas être null");
		socket = new DatagramSocket();
		addr = a;
		listener = l;
		
		Logger.info("Connexion au serveur à l'adresse "+addr.toString());
		socket.connect(addr);
		
		receiverThread = new Thread(() -> {
			DatagramPacket packet = new DatagramPacket(new byte[MegaSpaceInvader.NETWORK_MAX_PACKET_SIZE], MegaSpaceInvader.NETWORK_MAX_PACKET_SIZE, addr);
			
				try {
					while(true) {
						socket.receive(packet);
						
						byte[] packetData = Arrays.copyOf(packet.getData(), packet.getLength());
						
						if (packetData.length < 5) {
							Logger.severe("Erreur protocole : le packet n'est pas assez long");
							continue;
						}
						
						int declaredSize = ByteBuffer.wrap(packetData, 1, 4).getInt();
						
						if (packetData.length != 5+declaredSize) {
							Logger.severe("Erreur protocole : le packet n'est pas de la bonne taille : "+declaredSize+" déclaré, "+(packetData.length-5)+" réel");
							continue;
						}
						
						
						try {
							interpreteReceivedMessage(packetData);
						} catch (Exception e) {
							System.err.println("erreur lors de la prise en charge du message du serveur");
							e.printStackTrace();
						}
						
					}
				} catch (SocketTimeoutException e) {
					System.err.println("Le serveur a prit trop de temps à répondre");
				} catch (Exception e) {
					e.printStackTrace();
				}
		});
		receiverThread.setName("Client Net");
		receiverThread.start();
		
		PacketClientJoin joinPacket = new PacketClientJoin();
		joinPacket.setPlayerName(name);
		send(joinPacket);
		
	}
	

	

	public void send(PacketClient packet) throws IOException {
		byte[] bytes = packet.constructAndGetDataPacket();
		socket.send(new DatagramPacket(bytes, bytes.length, addr));
	}
	public void silentSend(PacketClient packet) {
		try {
			send(packet);
		} catch (IOException e) {
			Logger.warning("IOException lors de l'envoi du packet "+packet.getClass().getName());
		}
	}
	
	

	

	public void setListener(NetworkReceiveListener l) {
		if (l == null) throw new IllegalArgumentException("null non permis");
		listener = l;
	}
	
	
	
	private void interpreteReceivedMessage(byte[] data) {
		

		if (listener == null)
			throw new InvalidServerMessage("Le serveur ne peut actuellement pas prendre en charge de nouvelles requêtes. Les listeners n'ont pas encore été définis");
		
		Packet p = Packet.constructPacket(data);
		
		if (!(p instanceof PacketServer))
			throw new InvalidServerMessage("Le type de packet reçu n'est pas un packet attendu : "+p.getClass().getCanonicalName());
		
		PacketServer ps = (PacketServer) p;
		
		listener.onReceivePacket(ps);
	}
	
	public SocketAddress getServerAddress() {
		return addr;
	}
	
	
	
	
	
	public static class InvalidServerMessage extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public InvalidServerMessage(String message) {
			super(message);
		}
	}
	
}
