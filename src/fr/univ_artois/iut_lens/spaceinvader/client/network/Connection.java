package fr.univ_artois.iut_lens.spaceinvader.client.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;

import fr.univ_artois.iut_lens.spaceinvader.network_packet.Packet;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClient;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientJoin;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServer;
import fr.univ_artois.iut_lens.spaceinvader.util.Logger;

public class Connection {
	
	private DatagramSocket socket;
	private SocketAddress addr;
	private Thread receiverThread;
	private Charset charset = Charset.forName("UTF-8");
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
			DatagramPacket packet = new DatagramPacket(new byte[4096], 4096, addr);
			
				try {
					while(true) {
						socket.receive(packet);
						
						String dataStr = new String(packet.getData(), charset).substring(0, packet.getLength());

						Logger.info("[Serveur] "+dataStr);
						
						String[] data = dataStr.split(":", 2);
						
						
						if (data.length != 2) {
							Logger.severe("message du serveur mal formé");
							continue;
						}
						
						
						try {
							interpreteReceivedMessage(Integer.parseInt(data[0]), data[1]);
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
		String out = packet.getCode()+":"+packet.getData();
		Logger.info("[Client] "+out);
		byte[] bytes = out.getBytes(charset);
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
	
	
	
	private void interpreteReceivedMessage(int code, String data) {
		

		if (listener == null)
			throw new InvalidServerMessage("Le serveur ne peut actuellement pas prendre en charge de nouvelles requêtes. Les listeners n'ont pas encore été définis");
		
		Packet p = Packet.constructPacket(code, data);
		
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
