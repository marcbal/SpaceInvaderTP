package fr.univ_artois.iut_lens.spaceinvader.client;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.client.network.Connection;
import fr.univ_artois.iut_lens.spaceinvader.client.network.NetworkReceiveListener;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientDisconnect;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServer;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.sprites_manager.Sprite;
import fr.univ_artois.iut_lens.spaceinvader.sprites_manager.SpriteStore;
import fr.univ_artois.iut_lens.spaceinvader.util.Logger;

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
 * @author Kevin Glass (original), Marc Baloup, Maxime Maroine
 */
public class Client extends Canvas implements NetworkReceiveListener {
	private static final long serialVersionUID = 1L;

	public static Client instance;

	/** The stragey that allows us to use accelerate page flipping */
	private BufferStrategy bufferStrategy;
	private JFrame container;
	
	private OnScreenDisplay onScreenDisplay;
	
	/** True if the game is currently "running", i.e. the game loop is looping */
	private AtomicBoolean gameRunning = new AtomicBoolean(true);
	
	/** Gestion des entités */
	public EntitiesManager entitiesManager = new EntitiesManager();
	
	private KeyInputHandler keyHandler = new KeyInputHandler();
	private boolean waitingForKeyPress = true;
	
	
	private Sprite background;
	
	
	public AtomicLong displayFrameDuration = new AtomicLong();
	public AtomicLong logicalFrameDuration = new AtomicLong();
	public AtomicLong logicalCollisionDuration = new AtomicLong();
	
	public final Connection connection;
	
	public final Thread graphicalThread;
	
	/**
	 * @throws IOException si un problème survient lors de la connexion
	 */
	public Client(SocketAddress serverAddress, String playerName) throws IOException {
		instance = this;
		
		Logger.info("Chargement de l'interface graphique ...");
		// create a frame to contain our game
		container = new JFrame("Méga Space Invader");
		
		// get hold the content of the frame and set up the resolution of the game
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(MegaSpaceInvader.DISPLAY_WIDTH,MegaSpaceInvader.DISPLAY_HEIGHT));
		panel.setLayout(null);
		
		
		// setup our canvas size and put it into the content of the frame
		setBounds(0,0,MegaSpaceInvader.DISPLAY_WIDTH,MegaSpaceInvader.DISPLAY_HEIGHT);
		panel.add(this);
		
		// Tell AWT not to bother repainting our canvas since we're
		// going to do that our self in accelerated mode
		setIgnoreRepaint(true);
		
		// finally make the window visible 
		container.pack();
		container.setResizable(false);
		container.setVisible(true);
		
		
		// add a listener to respond to the user closing the window. If they
		// do we'd like to exit the game
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				gameRunning.set(false);
			}
		});
		container.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent event) { }
			
			@Override
			public void componentResized(ComponentEvent event) { }
			
			@Override
			public void componentHidden(ComponentEvent event) {
				keyHandler.manualToggle("pause", true);
			}

			@Override
			public void componentMoved(ComponentEvent event) { }
		});
		
		// add a key input system (defined below) to our canvas
		// so we can respond to key pressed
		addKeyListener(keyHandler);
		
		// request the focus so key events come to us
		requestFocus();

		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics
		createBufferStrategy(2);
		bufferStrategy = getBufferStrategy();
		
		
		onScreenDisplay = new OnScreenDisplay();
		
		
		
		graphicalThread = new Thread(run());
		graphicalThread.setName("Client");
		graphicalThread.setPriority(Thread.MAX_PRIORITY);
		
		/*
		 * Connexion au serveur
		 */
		
		Logger.info("Tentative de connexion au serveur "+serverAddress);
		connection = new Connection(serverAddress, playerName, this);
		
	}
	

	
	
	
	
	private void updateDisplay() {
		

		
		// Get hold of a graphics context for the accelerated 
		// surface and blank it out
		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		g.setBackground(Color.BLACK);
		
		
		// dessin du background
		background = SpriteStore.get().getSprite("sprites/background_1080p.jpg");
		background.draw(g,
				(int)(MegaSpaceInvader.DISPLAY_WIDTH/2D - background.getWidth()/2D),
				(int)(MegaSpaceInvader.DISPLAY_HEIGHT/2D - background.getHeight()/2D));

		
		
		
		
		//Afficher les entités
        entitiesManager.draw(g);
		
		// if we're waiting for an "any key" press then draw the 
		// current message 
		if (waitingForKeyPress) {
			onScreenDisplay.drawMiddleWaiting(g);
		}
		
		// affiche le message comme quoi on est en pause
		onScreenDisplay.drawMiddlePause(g);
		
		onScreenDisplay.drawOther(g);
		
		// finally, we've completed drawing so clear up the graphics
		// and flip the buffer over
		g.dispose();
		bufferStrategy.show();
	}
	
	
	
	
	

	public KeyInputHandler getKeyInputHandler() { return keyHandler; }






	@Override
	public void onReceivePacket(PacketServer packet) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	private Runnable run() {
		return () -> {
			Logger.info("Interface graphique démarrée à "+MegaSpaceInvader.CLIENT_FRAME_PER_SECOND+" FPS");
			
			long delta = (long)(1000000000/MegaSpaceInvader.CLIENT_FRAME_PER_SECOND);
			// keep looping round til the game ends
			while (gameRunning.get()) {
				long loop_start = System.nanoTime();
	            updateDisplay();
	            
	            displayFrameDuration.set(System.nanoTime()-loop_start);
				try { Thread.sleep((delta-(displayFrameDuration.get()))/1000000); } catch (Exception e) {}
			}
			
			
			

			Logger.info("Déconnexion du serveur");
			connection.silentSend(new PacketClientDisconnect());
			
			
			Logger.info("Arrêt de l'interface graphique");
		};
	}
	
	public void start() {
		graphicalThread.start();
	}
	
	
	
	
}
