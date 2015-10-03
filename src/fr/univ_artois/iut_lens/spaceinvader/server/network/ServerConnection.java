package fr.univ_artois.iut_lens.spaceinvader.server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.charset.Charset;

import fr.univ_artois.iut_lens.spaceinvader.network_packet.Packet;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClient;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerProtocolError;


/**
 * <h1>Protocole de communication entre le serveur et un client</h1>
 * <p>
 * <b>ABC:Data</b><br/>
 * <br/>
 * A = 1 si serveur, 2 si client<br/>
 * B = 1 si normal, 2 si warning, 3 si erreur<br/>
 * C de 0 à 9
 * </p>
 * <p><b>Packet client</b></p>
 * <ul>
 * 	<li><code>210:Player name</code> Connexion avec nom du joueur</li>
 * 	<li><code>211:&lt;cmd&gt;</code> état des commandes, envoyé à chaque tick<br/>
 * 		première lettre : 'l', 'r' ou '-' pour la direction<br/>
 * 		deuxième lettre : 's' ou '-' pour le tir
 *  </li>
 *  <li><code>212:</code> Activer / désactiver la pause</li>
 *  <li><code>213:</code> Passer au niveau suivant</li>
 * 	<li><code>214:</code> Déconnexion</li>
 * </ul>
 * <p><b>Packet serveur</b></p>
 * <ul>
 * 	<li><code>110:</code> Connexion OK</li>
 * 	<li><code>120:Message</code> Erreur de protocole</li>
 * 	<li><code>130:Message</code> Impossible de rejoindre le serveur</li>
 *  <li><code>111:JSONString</code> Envoi au client les données relatif aux sprites (identifiant -> nom du fichier image)</li>
 *  <li><code>112:</code> lance un nouveau niveau. Réinitialise le contenu de la map : vide la liste des entités côté client</li>
 *  <li><code>113:JSONString</code> envoi/mise à jour des données d'entités</li>
 *  <li><code>114:JSONString</code> envoi/mise à jour des infos de la partie</li>
 *  <li><code>118:JSONString</code> Fin du niveau : score des joueurs</li>
 * 	<li><code>119:</code> Déconnexion OK</li>
 * 	<li><code>139:</code> Déconnecté pour cause de timeout</li>
 * </ul>
 * 
 * @author Marc Baloup
 *
 */
public class ServerConnection {
	
	public static final int MAX_PACKET_SIZE = 16384;
	
	private DatagramSocket socket;
	private Thread receiverThread;
	private Charset charset = Charset.forName("UTF-8");
	private NetworkReceiveListener gameListener;
	
	
	
	
	public ServerConnection(int port) throws IOException {
		if (port <= 0 || port > 65535)
			throw new IllegalArgumentException("le numéro de port est invalide");
		socket = new DatagramSocket(port);
		
		receiverThread = new Thread(() -> {
			DatagramPacket packet = new DatagramPacket(new byte[MAX_PACKET_SIZE], MAX_PACKET_SIZE);
			
			try {
				while(true) {
					socket.receive(packet);
					
					String dataStr = new String(packet.getData(), charset).substring(0, packet.getLength());

					System.out.println("[Client "+packet.getSocketAddress()+"] "+dataStr);
					
					String[] data = dataStr.split(":", 2);
					
					
					if (data.length != 2) {
						System.err.println("message du client mal formé");
						sendProtocolError(packet.getSocketAddress(), "Erreur protocole : le packet doit contenir au moins un symbole \":\"");
						continue;
					}
					
					int code = 0;
					try {
						code = Integer.parseInt(data[0]);
					} catch (NumberFormatException e) {
						System.err.println("message du client mal formé");
						sendProtocolError(packet.getSocketAddress(), "Erreur protocole : la première partie doit être un entier");
						continue;
					}
					
					
					try {
						interpreteReceivedMessage(packet.getSocketAddress(), code, data[1]);
					} catch (InvalidClientMessage e) {
						System.err.println("message du client mal formé");
						sendProtocolError(packet.getSocketAddress(), "Erreur protocole : "+e.getMessage());
					} catch (Exception e) {
						System.err.println("erreur lors de la prise en charge du message du client");
						e.printStackTrace();
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		receiverThread.start();
		
	}
	
	
	private void sendProtocolError(SocketAddress socketAddress, String string) throws IOException {
		PacketServerProtocolError packet = new PacketServerProtocolError();
		packet.setMessage(string);
		send(socketAddress, packet);
	}


	private void send(SocketAddress addr, Packet p) throws IOException {
		String out = p.getCode()+":"+p.getData();
		System.out.println("[Serveur à "+addr+"] "+out);
		byte[] bytes = out.getBytes(charset);
		socket.send(new DatagramPacket(bytes, bytes.length, addr));
	}
	
	
	private synchronized void interpreteReceivedMessage(SocketAddress addr, int code, String data) {
		
		if (gameListener == null)
			throw new InvalidClientMessage("Le serveur ne peut actuellement pas prendre en charge de nouvelles requêtes. Les listeners n'ont pas encore été définis");
		
		Packet p = Packet.constructPacket(code, data);
		
		if (!(p instanceof PacketClient))
			throw new InvalidClientMessage("Le type de packet reçu n'est pas un packet attendu : "+p.getClass().getCanonicalName());
		
		// PacketClient pc = (PacketClient) p;
		
		// TODO traitement
	}
	
	

	public void setListener(NetworkReceiveListener l) {
		if (l == null) throw new IllegalArgumentException("null non permis");
		gameListener = l;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public static class InvalidClientMessage extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public InvalidClientMessage(String message) {
			super(message);
		}
	}
	
}
