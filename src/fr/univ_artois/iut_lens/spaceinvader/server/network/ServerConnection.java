package fr.univ_artois.iut_lens.spaceinvader.server.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import fr.univ_artois.iut_lens.spaceinvader.network_packet.Packet;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClient;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServer;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerDisconnectOk;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerProtocolError;
import fr.univ_artois.iut_lens.spaceinvader.server.Server;
import fr.univ_artois.iut_lens.spaceinvader.util.Logger;


/**
 * <h1>Protocole de communication entre le serveur et un client</h1>
 * <p>
 * <b>Code du packet :</b><br/>
 * Légende : <code>0xXY</code>
 * <table border>
 * 	<tr><td><code>X</code></td>	<td>Client</td>	<td>Serveur</td></tr>
 * 	<tr><td>Normal</td>  		<td>0</td>		<td>4</td></tr>
 * 	<tr><td>Warning</td>		<td>1</td>		<td>5</td></tr>
 * 	<tr><td>Erreur</td>			<td>2</td>		<td>6</td></tr>
 * </table>
 * <code>Y</code> de 0 à F
 * </p>
 * <p><b>Packet client</b></p>
 * <ul>
 * 	<li><code>0x00:Player name</code> Connexion avec nom du joueur</li>
 * 	<li><code>0x01:&lt;cmd&gt;</code> état des commandes, envoyé à chaque tick (gauche droite, tir)</li>
 *  <li><code>0x02:</code> Activer / désactiver la pause</li>
 *  <li><code>0x03:</code> Passer au niveau suivant</li>
 * 	<li><code>0x0F:</code> Déconnexion</li>
 * </ul>
 * <p><b>Packet serveur</b></p>
 * <ul>
 * 	<li><code>0x40:</code> Connexion OK</li>
 * 	<li><code>0x50:Message</code> Erreur de protocole</li>
 * 	<li><code>0x60:Message</code> Impossible de rejoindre le serveur</li>
 * 
 *  <li><code>0x41:</code> informe les joueurs de l'état de pause du jeu</li>
 *  <li><code>0x42:</code> lance un nouveau niveau. Réinitialise le contenu de la map : vide la liste des entités côté client</li>
 *  <li><code>0x43:</code> envoi/mise à jour des données d'entités (ajout / déplacement / suppression) et des sprites</li>
 *  <li><code>0x44:</code> envoi/mise à jour des infos de la partie</li>
 *  <li><code>0x4E:</code> Fin du niveau : score des joueurs</li>
 *  
 * 	<li><code>0x4F:</code> Déconnexion OK</li>
 * 	<li><code>0x6F:</code> Déconnecté pour cause de timeout</li>
 * </ul>
 * 
 * @author Marc Baloup
 *
 */
public class ServerConnection {
	private static AtomicInteger connectionCounterId = new AtomicInteger(0);
	
	
	private ServerSocket socket;
	private Thread receiverThread;
	private NetworkReceiveListener gameListener;
	
	
	
	
	public ServerConnection(int port) throws IOException {
		if (port <= 0 || port > 65535)
			throw new IllegalArgumentException("le numéro de port est invalide");
		socket = new ServerSocket(port);
		
		receiverThread = new ServerConnectionThread();
		receiverThread.start();
		
	}
	
	/*
	 * Thread qui prends en charge les nouvelles connexions
	 */
	private class ServerConnectionThread extends Thread {
		
		public ServerConnectionThread() {
			super("Server Net");
		}
		
		@Override
		public void run() {

			try {
				while(true) {
					Socket socketClient = socket.accept();
					
					try {
						new ConnectionThread(socketClient).start();
					} catch(IOException e) {
						Logger.severe("Connexion impossible avec "+socketClient.getInetAddress());
					}
				}
			} catch (Exception e) {
				Logger.warning("Plus aucune connexion ne peux être acceptée : "+e.toString());
			}
		}
	}
	
	
	
	public class ConnectionThread extends Thread {
		private Socket socket;
		private Object outSynchronizer = new Object();
		private InputStream in;
		private OutputStream out;
		private SocketAddress address;
		
		public ConnectionThread(Socket s) throws IOException {
			super("Server Net Sock#"+connectionCounterId.getAndIncrement());
			socket = s;
			in = socket.getInputStream();
			out = socket.getOutputStream();
			address = new InetSocketAddress(socket.getInetAddress(), socket.getPort());
		}
		
		@Override
		public void run() {
			try {
				byte[] code = new byte[1];
				while(!socket.isClosed() && in.read(code) != -1) {
					byte[] sizeB = new byte[4];
					if (in.read(sizeB) != 4)
						throw new IOException("Socket "+address+" fermé");
					
					int size = ByteBuffer.wrap(sizeB).getInt();
					
					byte[] content = new byte[size];
	
					forceReadBytes(content);
					
					byte[] packetData = ByteBuffer.allocate(1+4+size).put(code).put(sizeB).put(content).array();
					
					
					try {
						interpreteReceivedMessage(this, packetData);
					} catch (InvalidClientMessage e) {
						Logger.severe("Message du client mal formé");
						sendProtocolError("Erreur protocole : "+e.getMessage());
					} catch (Exception e) {
						Logger.severe("Erreur lors de la prise en charge du message par le serveur");
						e.printStackTrace();
					}
				}
				
				
				
				
			} catch (IOException e) {
				Logger.severe("Fermeture de la connexion de "+address+" : "+e);
			}
		}
		
		public void send(PacketServer p) {
			synchronized (outSynchronizer) {
				try {
					out.write(p.constructAndGetDataPacket());
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
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
		
		public void sendProtocolError(String string) throws IOException {
			PacketServerProtocolError packet = new PacketServerProtocolError();
			packet.setMessage(string);
			send(packet);
			close();
		}
		
		public void close() {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	
	
	private synchronized void interpreteReceivedMessage(ConnectionThread co, byte[] data) {
		
		if (gameListener == null)
			throw new InvalidClientMessage("Le serveur ne peut actuellement pas prendre en charge de nouvelles requêtes. Les listeners n'ont pas encore été définis");
		
		Packet p = Packet.constructPacket(data);
		
		// Logger.info("(Serveur) <- "+p.getClass().getSimpleName()+" <- (Client "+addr+")");
		
		if (!(p instanceof PacketClient))
			throw new InvalidClientMessage("Le type de packet reçu n'est pas un packet attendu : "+p.getClass().getCanonicalName());
		
		PacketClient pc = (PacketClient) p;
		
		gameListener.onReceivePacket(co, pc);
	}
	
	

	public void setListener(NetworkReceiveListener l) {
		if (l == null) throw new IllegalArgumentException("null non permis");
		gameListener = l;
	}
	
	public void close() {
		Server.serverInstance.playerManager.sendToAll(new PacketServerDisconnectOk());
		try {
			socket.close();
		} catch (IOException e) { }
	}
	
	
	
	
	
	
	
	
	
	
	public static class InvalidClientMessage extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public InvalidClientMessage(String message) {
			super(message);
		}
	}
	
}
