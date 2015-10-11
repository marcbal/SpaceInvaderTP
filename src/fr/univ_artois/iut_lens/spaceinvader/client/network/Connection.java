package fr.univ_artois.iut_lens.spaceinvader.client.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.Packet;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClient;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientJoin;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServer;
import fr.univ_artois.iut_lens.spaceinvader.server.network.ServerConnection.InvalidClientMessage;
import fr.univ_artois.iut_lens.spaceinvader.util.Logger;

public class Connection {
	
	private Socket socket;
	private SocketAddress addr;
	public final ClientConnectionThread socketThread;
	private NetworkReceiveListener listener;
	
	
	
	
	public Connection(InetSocketAddress a, String name, NetworkReceiveListener l) throws IOException {
		if (a == null || name == null || l == null)
			throw new IllegalArgumentException("les arguments ne peuvent pas être null");
		socket = new Socket();
		socket.setReceiveBufferSize(MegaSpaceInvader.NETWORK_TCP_BUFFER_SIZE);
		socket.setSendBufferSize(MegaSpaceInvader.NETWORK_TCP_BUFFER_SIZE);
		socket.connect(a);
		addr = a;
		listener = l;
		
		Logger.info("Connexion au serveur à l'adresse "+addr.toString());
		
		socketThread = new ClientConnectionThread(socket);
		socketThread.setName("Client Net");
		socketThread.start();
		
		PacketClientJoin joinPacket = new PacketClientJoin();
		joinPacket.setPlayerName(name);
		send(joinPacket);
		
	}
	
	public class ClientConnectionThread extends Thread {
		

		private Socket socket;
		private Object outSynchronizer = new Object();
		private InputStream in;
		private OutputStream out;
		
		public ClientConnectionThread(Socket s) throws IOException {
			super("Client Net");
			socket = s;
			in = socket.getInputStream();
			out = socket.getOutputStream();
		}
		
		
		@Override
		public void run() {
			
			try {
				byte[] code = new byte[1];
				while(!socket.isClosed() && in.read(code) != -1) {
					byte[] sizeB = new byte[4];
					if (in.read(sizeB) != 4)
						throw new IOException("Socket "+addr+" fermé");
					
					int size = ByteBuffer.wrap(sizeB).getInt();
					
					byte[] content = new byte[size];
					
					forceReadBytes(content);
					
					byte[] packetData = ByteBuffer.allocate(1+4+size).put(code).put(sizeB).put(content).array();
					
					
					try {
						interpreteReceivedMessage(packetData);
					} catch (InvalidClientMessage e) {
						Logger.severe("Message du client mal formé");
					} catch (Exception e) {
						Logger.severe("Erreur lors de la prise en charge du message par le serveur");
						e.printStackTrace();
					}
				}
				
			} catch (SocketTimeoutException e) {
				System.err.println("Le serveur a prit trop de temps à répondre");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		private void forceReadBytes(byte[] buff) throws IOException {
			int pos = 0;
			do {
				int nbR = in.read(buff, pos, buff.length-pos);
				if (nbR == -1)
					throw new IOException("Can't read required amount of byte");
				pos += nbR;
			} while (pos < buff.length);
		}
		
		public void send(PacketClient packet) throws IOException {
			synchronized (outSynchronizer) {
				out.write(packet.constructAndGetDataPacket());
				out.flush();
			}
		}
		
		public void close() {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	

	public void send(PacketClient packet) throws IOException {
		socketThread.send(packet);
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
