package fr.univ_artois.iut_lens.spaceinvader;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.univ_artois.iut_lens.spaceinvader.entities.*;
import fr.univ_artois.iut_lens.spaceinvader.entities.ship.EntityShipFinal;
import fr.univ_artois.iut_lens.spaceinvader.entities.shot.EntityShotFromAllyFinal;

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
 * @author Kevin Glass
 */
public class Game extends Canvas {
	private static final long serialVersionUID = 1L; // corrige un warning

	private int window_width = 800;
	private int window_height = 600;
	
	static boolean multiThread = true;
	
	public static Game gameInstance;
	
	private static long currentNanoTime = 10000000000L;

	/** The stragey that allows us to use accelerate page flipping */
	private BufferStrategy bufferStrategy;
	
	private OnScreenDisplay onScreenDisplay;
	
	/** True if the game is currently "running", i.e. the game loop is looping */
	private boolean gameRunning = true;
	
	private float fps = 60;
	
	/** Gestion des entités */
	private EntitiesManager entitiesManager = new EntitiesManager();
	
	/** gestion des niveaux */
	private LevelManager levelManager = new LevelManager(entitiesManager);
	
	/** gestion du vaisseau */
	private ShipManager shipManager = new ShipManager(entitiesManager);
	
	/** gestion des bonus */
	private BonusManager bonusManager = new BonusManager(entitiesManager, shipManager);
	
	private KeyInputHandler keyHandler = new KeyInputHandler();
	private boolean waitingForKeyPress = true;
	
	
	private Sprite background = SpriteStore.get().getSprite("sprites/background.jpg");
	
	/**
	 * Construct our game and set it running.
	 */
	public Game() {
		gameInstance = this;
		// create a frame to contain our game
		JFrame container = new JFrame("Mega Space Invader");
		
		// get hold the content of the frame and set up the resolution of the game
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(800,600));
		panel.setLayout(null);
		
		
		// setup our canvas size and put it into the content of the frame
		setBounds(0,0,800,600);
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
				System.exit(0);
			}
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
		
		
		
		// initialise the entities in our game so there's something
		// to see at startup
		startLevel();
		
		if (multiThread)
		{
			Thread th = new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					long delta = (long)(1/fps*1000000000);
					// keep looping round til the game ends
					while (gameRunning) {
						long loop_start = System.nanoTime();
			            updateDisplay();
						try { Thread.sleep((delta-(System.nanoTime()-loop_start))/1000000); } catch (Exception e) {}
					}
				}
			});
			th.setName("Rendering Thread");
			th.setPriority(Thread.MAX_PRIORITY);
			th.start();
			
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
			Thread.currentThread().setName("Main Thread");
		}
		
		
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

			if(!multiThread)
				updateDisplay();
            
			try { Thread.sleep((delta-(System.nanoTime()-loop_start))/1000000); } catch (Exception e) {}
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
			entitiesManager.moveEntities(delta,levelManager);
			
			//Vérifier si il y a eu des collisions
			//Supprimer les entités tués
			entitiesManager.doCollisions();
			
			//Faire tirer les entités
			entitiesManager.makeEntitiesShoot(levelManager);
			
			
			//Générer des bonus
			bonusManager.performBonus();
			
			shipManager.makeItEvolve();
			// réinitialiser le déplacement du vaisseau
			shipManager.moveShip(0);
		}
		
	}
	
	
	private void updateDisplay() {
		

		
		// Get hold of a graphics context for the accelerated 
		// surface and blank it out
		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		
		background.draw(g, 0, 0);

		//Afficher les entités
        entitiesManager.draw(g);
		
		// if we're waiting for an "any key" press then draw the 
		// current message 
		if (waitingForKeyPress) {
			onScreenDisplay.drawMiddleWaiting(g);
		}
		
		// affiche le message comme quoi on est en pause
		onScreenDisplay.drawMiddlePause(g);
		
		onScreenDisplay.drawTopLeft(g);
		
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
		EntityShipFinal.reset();
		EntityShotFromAllyFinal.reset();
		entitiesManager.getEntitiesList().clear();
		// Placer le vaisseau pr�alablement cr�er dans le tableau des entit�s
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
		entitiesManager.getEntitiesList().clear(); //nettoie l'écran du vaisseau
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
