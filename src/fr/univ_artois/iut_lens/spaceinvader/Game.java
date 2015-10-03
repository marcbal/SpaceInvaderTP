package fr.univ_artois.iut_lens.spaceinvader;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.concurrent.atomic.AtomicLong;

import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.univ_artois.iut_lens.spaceinvader.client_game.KeyInputHandler;
import fr.univ_artois.iut_lens.spaceinvader.entities.*;
import fr.univ_artois.iut_lens.spaceinvader.entities.ship.EntityShip;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.LevelManager;

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
public class Game extends Canvas {
	private static final long serialVersionUID = 1L; // corrige un warning
	
	private int real_window_width = 960;
	private int real_window_height = 540;

	private int window_width = real_window_width;
	private int window_height = real_window_height;
	
	private int window_border_width;
	private int window_border_height;
	
	public static Game gameInstance;
	
	private static long currentNanoTime = 10000000000L;

	/** The stragey that allows us to use accelerate page flipping */
	private BufferStrategy bufferStrategy;
	private JFrame container;
	
	private OnScreenDisplay onScreenDisplay;
	
	/** True if the game is currently "running", i.e. the game loop is looping */
	private boolean gameRunning = true;
	
	public float fps = 60;
	
	/** Gestion des entités */
	public EntitiesManager entitiesManager = new EntitiesManager();
	
	/** gestion des niveaux */
	private LevelManager levelManager;
	
	/** gestion du vaisseau */
	private ShipManager shipManager;
	
	/** gestion des bonus */
	private BonusManager bonusManager;
	
	private KeyInputHandler keyHandler = new KeyInputHandler();
	private boolean waitingForKeyPress = true;
	
	
	private Sprite background;
	
	
	public AtomicLong displayFrameDuration = new AtomicLong();
	public AtomicLong logicalFrameDuration = new AtomicLong();
	public AtomicLong logicalCollisionDuration = new AtomicLong();
	
	/**
	 * Construct our game and set it running.
	 */
	public Game() {
		gameInstance = this;
		// create a frame to contain our game
		container = new JFrame("Mega Space Invader");
		
		// get hold the content of the frame and set up the resolution of the game
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(window_width,window_height));
		panel.setLayout(null);
		
		
		// setup our canvas size and put it into the content of the frame
		setBounds(0,0,window_width,window_height);
		panel.add(this);
		
		// Tell AWT not to bother repainting our canvas since we're
		// going to do that our self in accelerated mode
		setIgnoreRepaint(true);
		
		// finally make the window visible 
		container.pack();
		container.setResizable(true);
		container.setVisible(true);
		
		window_border_width = container.getWidth() - window_width;
		window_border_height = container.getHeight() - window_height;
		
		
		// add a listener to respond to the user closing the window. If they
		// do we'd like to exit the game
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		container.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent event) { }
			
			@Override
			public void componentResized(ComponentEvent event) {
				Component c = event.getComponent();
				setBounds(0, 0, c.getWidth(), c.getHeight());
				real_window_width = c.getWidth() - window_border_width;
				real_window_height = c.getHeight() - window_border_height;
			}
			
			@Override
			public void componentHidden(ComponentEvent event) {
				keyHandler.manualToggle("pause", true);
				System.out.println("hidden");
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
		levelManager = new LevelManager(entitiesManager);
		shipManager = new ShipManager(entitiesManager);
		bonusManager = new BonusManager(entitiesManager, shipManager);
		
		
		// initialise the entities in our game so there's something
		// to see at startup
		startLevel();
		
		Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				long delta = (long)(1/fps*1000000000);
				// keep looping round til the game ends
				while (gameRunning) {
					long loop_start = System.nanoTime();
		            updateDisplay();
		            
		            displayFrameDuration.set(System.nanoTime()-loop_start);
					try { Thread.sleep((delta-(displayFrameDuration.get()))/1000000); } catch (Exception e) {}
				}
			}
		});
		th.setName("Rendering Thread");
		th.setPriority(Thread.MAX_PRIORITY);
		th.start();
		
		Thread.currentThread().setName("Main Thread");
		
		
	}
	

	
	/**
	 * The main game loop. This loop is running during all game
	 * play as is responsible for the following activities:
	 * <p>
	 * - Working out the speed of the game loop to update moves
	 * - Moving the game entities
	 * - Drawing the screen contents (entities, text)
	 * - Updating game events
	 * - Checking Input
	 * <p>
	 */
	public void gameLoop() {
		
		long delta = (long) (1/fps*1000000000);
		// keep looping round til the game ends
		while (gameRunning) {
			long loop_start = System.nanoTime();
			if (!keyHandler.isKeyToggle("pause"))
				currentNanoTime += delta;
			
			handleEvent();
			
			updateLogic(delta);
            logicalFrameDuration.set(System.nanoTime()-loop_start);
			try { Thread.sleep((delta-(logicalFrameDuration.get()))/1000000); } catch (Exception e) {}
		}
	}
	
	private void handleEvent()
	{
		// resolve the movement of the ship. First assume the ship 
		// isn't moving. If either cursor key is pressed then
		// update the movement appropraitely
		
		if (keyHandler.isKeyWaitPress("start") && waitingForKeyPress)
		{
			waitingForKeyPress = false;
			
			window_width = real_window_width;
			window_height = real_window_height;
			
			startLevel();
		}
			
		
		if (keyHandler.isKeyPressed("shipLeft") && !keyHandler.isKeyPressed("shipRight")) {
			shipManager.moveShip(-1);
		} else if (keyHandler.isKeyPressed("shipRight") && !keyHandler.isKeyPressed("shipLeft")) {
			shipManager.moveShip(1);
		}
		
		// if we're pressing fire, attempt to fire
		if (keyHandler.isKeyPressed("shipFire") && !keyHandler.isKeyToggle("pause")  && !waitingForKeyPress) {
			shipManager.tryToShoot(getCurrentNanoTime()/1000000/* convert to milliseconds */);
		}
	}
	
	
	
	private void updateLogic(long delta) {
		if (!waitingForKeyPress && !keyHandler.isKeyToggle("pause"))
		{
			//Déplacer les entités
			//Vérifier si il y a eu des collisions
			//Supprimer les entités tués
			entitiesManager.moveAndCollideEntities(delta,levelManager);
			
			//Faire tirer les entités
			entitiesManager.makeEntitiesShoot(levelManager);
			
			
			//Générer des bonus
			bonusManager.performBonus();
			
			shipManager.makeItEvolve();
			// réinitialiser le déplacement du vaisseau
			shipManager.moveShip(0);
		}
		else
			logicalCollisionDuration.set(0);
		
	}
	
	
	private void updateDisplay() {
		

		
		// Get hold of a graphics context for the accelerated 
		// surface and blank it out
		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		g.setBackground(Color.BLACK);
		
		
		// dessin du background
		if (real_window_width > 1920 || real_window_height > 1080)
			background = SpriteStore.get().getSprite("sprites/background_4K.jpg");
		else
			background = SpriteStore.get().getSprite("sprites/background_1080p.jpg");
		background.draw(g,
				(int)(real_window_width/2D - background.getWidth()/2D),
				(int)(real_window_height/2D - background.getHeight()/2D));

		
		
		
		
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
	
	
	
	
	
	
	/**
	 * Start a fresh game, this should clear out any old data and
	 * create a new set.
	 */
	private void startLevel() {
		entitiesManager.getEntitiesList().clear();
		
		shipManager.reinitAllShip();
		
		// Placer le vaisseau préalablement créer dans le tableau des entités
		EntityShip ship = shipManager.getCurrentShip();
		ship.getPosition().x = window_width/2D - ship.getBoundingBox().width/2D;
		ship.getPosition().y = window_height-50;
		entitiesManager.getEntitiesList().add(shipManager.getCurrentShip());
		
		// create block of aliens, with the arguments
		
		entitiesManager.getEntitiesList().addAll(levelManager.getCurrentLevel().generateLevel());
		
		onScreenDisplay.setLevelMaxLife(entitiesManager.getTotalRemainingEnnemyLife());
	}
	
	/**
	 * Notification that the player has died. 
	 */
	public void notifyDeath() {
		onScreenDisplay.setMiddleMessage("Oh non ! Ils vous ont battu ! :( Réessayez ?");
		levelManager.goToFirstLevel();
		shipManager.decreaseShipType();
		entitiesManager.getEntitiesList().clear(); //nettoie l'écran des entités
		waitingForKeyPress = true;
	}
	
	/**
	 * Notification that the player has won since all the aliens
	 * are dead.
	 */
	public void notifyWin() {
		entitiesManager.getEntitiesList().clear(); //nettoie l'écran du vaisseau
		onScreenDisplay.setMiddleMessage("Bien joué ! Vous avez gagné :)");
		levelManager.goToNextLevel();
		//shipManager.increaseShipType(); //Evolution du vaisseau
		waitingForKeyPress = true;
	}
	
	/**
	 * Notification that an alien has been killed
	 */
	public synchronized void notifyAlienKilled() {
		
		//If the last alien as been killed
		if (!levelManager.getCurrentLevel().hasOneDestroyed()) {
			notifyWin();
		}
		
		// if there are still some aliens left then they all need to get faster, so
		// speed up all the existing aliens
		for(Entity entity : entitiesManager.getEntitiesList()) {
		    entity.setNotifyAlienKilled();
		}
	}
	
	

	public int getWindowWidth() { return window_width; }
	public int getWindowHeight() { return window_height; }
	public static long getCurrentNanoTime() { return currentNanoTime; }

	public EntitiesManager getEntitiesManager() { return entitiesManager; }
	public LevelManager getLevelManager() { return levelManager; }
	public ShipManager getShipManager() { return shipManager; }
	public KeyInputHandler getKeyInputHandler() { return keyHandler; }
	
	
	
	/**
	 * The entry point into the game. We'll simply create an
	 * instance of class which will start the display and game
	 * loop.
	 * 
	 * @param argv The arguments that are passed into our game
	 */
	public static void main(String argv[]) {
		

		
		
		
		
		
		
		
		Game g =new Game();

		// Start the main game loop, note: this method will not
		// return until the game has finished running. Hence we are
		// using the actual main thread to run the game.
		g.gameLoop();
		
		
		
		
		
	}
}
