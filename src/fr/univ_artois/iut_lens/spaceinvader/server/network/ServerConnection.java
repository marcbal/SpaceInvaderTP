package fr.univ_artois.iut_lens.spaceinvader.server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.Packet;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClient;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerProtocolError;
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
	
	private DatagramSocket socket;
	private Thread receiverThread;
	private NetworkReceiveListener gameListener;
	
	
	
	
	public ServerConnection(int port) throws IOException {
		if (port <= 0 || port > 65535)
			throw new IllegalArgumentException("le numéro de port est invalide");
		socket = new DatagramSocket(port);
		
		receiverThread = new Thread(() -> {
			DatagramPacket packet = new DatagramPacket(new byte[MegaSpaceInvader.NETWORK_MAX_PACKET_SIZE], MegaSpaceInvader.NETWORK_MAX_PACKET_SIZE);
			
			try {
				while(true) {
					socket.receive(packet);
					
					byte[] packetData = Arrays.copyOf(packet.getData(), packet.getLength());
					
					if (packetData.length < 5) {
						sendProtocolError(packet.getSocketAddress(), "Erreur protocole : le packet n'est pas assez long");
						continue;
					}
					
					int declaredSize = ByteBuffer.wrap(packetData, 1, 4).getInt();
					
					if (packetData.length != 5+declaredSize) {
						sendProtocolError(packet.getSocketAddress(), "Erreur protocole : le packet n'est pas de la bonne taille : "+declaredSize+" déclaré, "+(packetData.length-5)+" réel");
						continue;
					}
					
					
					try {
						interpreteReceivedMessage(packet.getSocketAddress(), packetData);
					} catch (InvalidClientMessage e) {
						Logger.severe("Message du client mal formé");
						sendProtocolError(packet.getSocketAddress(), "Erreur protocole : "+e.getMessage());
					} catch (Exception e) {
						Logger.severe("Erreur lors de la prise en charge du message par le serveur");
						e.printStackTrace();
					}
					
				}
			} catch (SocketException e) {
				Logger.warning("Plus aucun packet ne peut être reçu du réseau");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		receiverThread.setName("Server Net");
		receiverThread.start();
		
	}
	
	
	private void sendProtocolError(SocketAddress socketAddress, String string) throws IOException {
		PacketServerProtocolError packet = new PacketServerProtocolError();
		packet.setMessage(string);
		send(socketAddress, packet);
	}


	public void send(SocketAddress addr, Packet p) throws IOException {
		byte[] bytes = p.constructAndGetDataPacket();
		Logger.info("(Serveur) -> "+p.getClass().getSimpleName()+" -> (Client "+addr+")");
		socket.send(new DatagramPacket(bytes, bytes.length, addr));
	}
	
	
	private synchronized void interpreteReceivedMessage(SocketAddress addr, byte[] data) {
		
		if (gameListener == null)
			throw new InvalidClientMessage("Le serveur ne peut actuellement pas prendre en charge de nouvelles requêtes. Les listeners n'ont pas encore été définis");
		
		Packet p = Packet.constructPacket(data);
		
		Logger.info("(Serveur) <- "+p.getClass().getSimpleName()+" <- (Client "+addr+")");
		
		if (!(p instanceof PacketClient))
			throw new InvalidClientMessage("Le type de packet reçu n'est pas un packet attendu : "+p.getClass().getCanonicalName());
		
		PacketClient pc = (PacketClient) p;
		
		gameListener.onReceivePacket(addr, pc);
	}
	
	

	public void setListener(NetworkReceiveListener l) {
		if (l == null) throw new IllegalArgumentException("null non permis");
		gameListener = l;
	}
	
	public void close() {
		socket.close();
	}
	
	
	
	
	
	
	
	
	
	
	public static class InvalidClientMessage extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public InvalidClientMessage(String message) {
			super(message);
		}
	}
	
}
