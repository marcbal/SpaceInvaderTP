package fr.univ_artois.iut_lens.spaceinvader.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.client.network.Connection;
import fr.univ_artois.iut_lens.spaceinvader.client.network.Connection.InvalidServerMessage;
import fr.univ_artois.iut_lens.spaceinvader.client.network.NetworkReceiveListener;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientCommand;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientCommand.Direction;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientDisconnect;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientNextLevel;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientPingReply;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientTogglePause;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServer;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerCantJoin;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerConnectionOk;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerDisconnectOk;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerDisconnectTimeout;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerLevelEnd;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerLevelEnd.PlayerScore;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerLevelStart;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerLog;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerPing;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerProtocolError;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerTogglePause;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateInfos;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateInfos.GameInfo;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap;
import fr.univ_artois.iut_lens.spaceinvader.sprites_manager.Sprite;
import fr.univ_artois.iut_lens.spaceinvader.sprites_manager.SpriteStore;
import fr.univ_artois.iut_lens.spaceinvader.util.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The main hook of our game. This class with both act as a manager
 * for the display and central mediator for the game logic. 
 * 
 * Display management will consist of a loop that cycles round all
 * entities in the game asking them to move and then drawing them
 * in the appropriate place. With the help of an inner class it
 * will also allow the player to control the main ship.
 * 
 * As a mediator it will be informed when entities within our game
 * detect events (e.g. alient killed, played died) and will take
 * appropriate game actions.
 * 
 * @author Kevin Glass (original), Maxime Maroine, Marc Baloup
 */
public class Client extends Canvas implements NetworkReceiveListener {

	public static Client instance;
	
	private Stage stage;
	
	private OnScreenDisplay onScreenDisplay;
	
	/** True if the game is currently "running", i.e. the game loop is looping */
	public AtomicBoolean gameRunning = new AtomicBoolean(true);
	
	/** Gestion des entités */
	public EntityRepresenterManager entityRepresenterManager = new EntityRepresenterManager();
	
	private KeyInputHandler keyHandler = new KeyInputHandler();
	private AtomicBoolean waitingForKeyPress = new AtomicBoolean(true);
	public AtomicBoolean serverSidePause = new AtomicBoolean(false);
	
	
	private Sprite background;
	
	
	public AtomicLong logicalFrameDuration = new AtomicLong();
	public AtomicLong logicalCollisionDuration = new AtomicLong();
	
	public Connection connection;
	
	public final InetSocketAddress serverAddress;
	
	public final String playerName;
	
	public AtomicReference<GameInfo> lastGameInfo = new AtomicReference<>(new GameInfo());
	
	/**
	 * @throws IOException si un problème survient lors de la connexion
	 */
	public Client(Stage primaryStage, InetSocketAddress serverAddress, String playerName) throws IOException {
		super(MegaSpaceInvader.DISPLAY_WIDTH, MegaSpaceInvader.DISPLAY_HEIGHT);
		instance = this;
		this.serverAddress = serverAddress;
		this.playerName = playerName;
		
		stage = primaryStage;
		
		
		// create a scene to contain our game
		stage.setScene(new Scene(new BorderPane(this)));
		stage.setTitle("Mega Space Invader");
		stage.setResizable(true);

		stage.sizeToScene();
		
		stage.setOnCloseRequest(event -> {
			if (event.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
				gameRunning.set(false);
			}
		});
		stage.iconifiedProperty().addListener((obj, oldV, newV) -> {
			if (newV) {
				keyHandler.manualToggle("pause", true);
			}
		});
		
		// add a key input system (defined below) to our canvas
		// so we can respond to key pressed
		setOnKeyPressed(keyHandler::keyPressed);
		setOnKeyReleased(keyHandler::keyReleased);
		
		stage.show();
		stage.centerOnScreen();
		
		

		background = SpriteStore.get().getSprite("sprites/background_1080p.jpg");
		
		
		// request the focus so key events come to us
		requestFocus();
		
		
		onScreenDisplay = new OnScreenDisplay();

		
		
		getGraphicsContext2D().setFontSmoothingType(FontSmoothingType.LCD);
		getGraphicsContext2D().setFont(Font.font("SansSerif", 12));
		

		widthProperty().bind(stage.getScene().widthProperty());
		heightProperty().bind(stage.getScene().heightProperty());
		
		
		Platform.runLater(() -> {
			onScreenDisplay.setMiddleMessage("Connexion à "+serverAddress+" ...");
	        updateDisplay(System.nanoTime()); // première affichage
	        
			/*
			 * Connexion au serveur
			 */
			
			try {
				connection = new Connection(serverAddress, playerName, this);
			} catch (IOException e) {
				onScreenDisplay.setMiddleMessage("Connexion impossible.");
		        updateDisplay(System.nanoTime());
		        try { Thread.sleep(5000); } catch (InterruptedException e1) { }
				stage.hide();
		        return;
			}
			
			AnimationTimer timer = new AnimationTimer() {
				
				@Override
				public void handle(long now) {
					if (!gameRunning.get()) {
						connection.silentSend(new PacketClientDisconnect());
						stage.hide();
						stop();
					}
					long loop_start = System.nanoTime();
		            updateDisplay(loop_start);
		            
		            // envoi des packets
		            sendPackets();
				}
			};
			timer.start();
			
		});
		
		
		
		
		
	}
	

	
	
	
	
	private void sendPackets() {
		
		// direction et tir du vaisseau, seulement si le jeu est en cours
		if (!waitingForKeyPress.get() && !serverSidePause.get()) {
			PacketClientCommand cmdPacket = new PacketClientCommand();
			Direction dir;
			if (keyHandler.isKeyPressed("shipLeft") && !keyHandler.isKeyPressed("shipRight"))
				dir = Direction.LEFT;
			else if (!keyHandler.isKeyPressed("shipLeft") && keyHandler.isKeyPressed("shipRight"))
				dir = Direction.RIGHT;
			else
				dir = Direction.NONE;
			cmdPacket.setDirection(dir);
			cmdPacket.setShooting(keyHandler.isKeyPressed("shipFire"));
			try {
				connection.send(cmdPacket);
			} catch (IOException e) {
				onScreenDisplay.setMiddleMessage("Connexion perdue");
				throw new RuntimeException("Erreur lors de l'envoi du packet de commande", e);
			}
		}
		
		if (keyHandler.isKeyToggled("pause") != serverSidePause.get()) {
			PacketClientTogglePause pausePacket = new PacketClientTogglePause();
			pausePacket.setPause(keyHandler.isKeyToggled("pause"));
			try {
				connection.send(pausePacket);
			} catch (IOException e) {
				throw new RuntimeException("Erreur lors de l'envoi du packet Pause", e);
			}
		}
		
		if (waitingForKeyPress.get() && keyHandler.isKeyWaitPress("start")) {
			try {
				connection.send(new PacketClientNextLevel());
			} catch (IOException e) {
				throw new RuntimeException("Erreur lors de l'envoi du packet NextLevel", e);
			}
		}
		else
			keyHandler.isKeyWaitPress("start"); // flush useless "start" key press
		
		
		
	}





	private void updateDisplay(long loop_start) {
		
		GraphicsContext g = getGraphicsContext2D();
		g.setTransform(new Affine()); // reset transform matrix
		g.setFill(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		

		// compute sizes for rescaling
		double zoom = Math.min(getWidth() / MegaSpaceInvader.DISPLAY_WIDTH,
				getHeight() / MegaSpaceInvader.DISPLAY_HEIGHT);
		double zoomImage = Math.max(getWidth() / background.getWidth(),
				getHeight() / background.getHeight());

		double translateX = (getWidth() - zoom * MegaSpaceInvader.DISPLAY_WIDTH) / 2;
		double translateY = (getHeight() - zoom * MegaSpaceInvader.DISPLAY_HEIGHT) / 2;
		
		// dessin du background
		g.drawImage(background.getImage(), 
				getWidth()/2D - background.getWidth()/2D*zoomImage,
				getHeight()/2D - background.getHeight()/2D*zoomImage,
				background.getWidth() * zoomImage,
				background.getHeight() * zoomImage);
		
		g.translate(translateX, translateY);
		g.scale(zoom, zoom);
		
		
		
		
		
		
		
		
		//Afficher les entités
		entityRepresenterManager.drawAll(g, loop_start);
		
		// if we're waiting for an "any key" press then draw the 
		// current message 
		if (waitingForKeyPress.get()) {
			onScreenDisplay.drawMiddleWaiting(g);
		}
		
		// affiche le message comme quoi on est en pause
		onScreenDisplay.drawMiddlePause(g);
		
		onScreenDisplay.drawOtherInfos(g);
		
		
	}
	
	
	
	
	

	public KeyInputHandler getKeyInputHandler() { return keyHandler; }






	@Override
	public void onReceivePacket(PacketServer packet) {
		if (packet instanceof PacketServerCantJoin) {
			gameRunning.set(false);
			Logger.severe("Impossible de rejoindre le serveur : "+((PacketServerCantJoin)packet).getReason());
		}
		else if (packet instanceof PacketServerConnectionOk) {
			onScreenDisplay.setMiddleMessage("Vous êtes maintenant connecté !");
		}
		else if (packet instanceof PacketServerPing) {
			PacketClientPingReply pong = new PacketClientPingReply();
			pong.setPingId(((PacketServerPing)packet).getPingId());
			try {
				connection.send(pong);
			} catch (IOException e) {
				Logger.severe("Impossible d'envoyer un pong : "+e);
				e.printStackTrace();
			}
		}
		else if (packet instanceof PacketServerProtocolError) {
			gameRunning.set(false);
			Logger.severe("Erreur de protocole Client -> Serveur : "+((PacketServerProtocolError)packet).getMessage());
		}
		else if (packet instanceof PacketServerDisconnectTimeout) {
			gameRunning.set(false);
			Logger.severe("Déconnecté par le serveur : Timeout");
			connection.close();
		}
		else if (packet instanceof PacketServerDisconnectOk) {
			connection.close();
		}
		else if (packet instanceof PacketServerLevelEnd) {
			waitingForKeyPress.set(true);
			List<PlayerScore> scores = ((PacketServerLevelEnd)packet).getScores();
			String infoScores = "";
			for (PlayerScore score : scores) {
				if (infoScores.length() != 0)
					infoScores += ", ";
				infoScores += score.playerName+" : "+score.score;
			}
			onScreenDisplay.setMiddleMessage("Le niveau est terminé : "+infoScores);
			entityRepresenterManager.clear();
		}
		else if (packet instanceof PacketServerLevelStart) {
			waitingForKeyPress.set(false);
			onScreenDisplay.setMiddleMessage("");
			entityRepresenterManager.clear();
		}
		else if (packet instanceof PacketServerUpdateInfos) {
			lastGameInfo.set(((PacketServerUpdateInfos)packet).getInfos());
		}
		else if (packet instanceof PacketServerUpdateMap) {
			entityRepresenterManager.putUpdateFromServer(((PacketServerUpdateMap)packet).getEntityData());
		}
		else if (packet instanceof PacketServerTogglePause) {
			boolean pause = ((PacketServerTogglePause)packet).getPause();
			keyHandler.manualToggle("pause",pause);
			serverSidePause.set(pause);
		}
		else if (packet instanceof PacketServerLog) {
			onScreenDisplay.addLogLine(((PacketServerLog)packet).getMessage());
		}
		else
			throw new InvalidServerMessage("Le packet n'est pas prise en charge : "+packet.getClass().getName());
	}
	
	
	
	
}
