package base;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

import entities.*;

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

	/** The stragey that allows us to use accelerate page flipping */
	private BufferStrategy bufferStrategy;
	
	/** True if the game is currently "running", i.e. the game loop is looping */
	private boolean gameRunning = true;
	
	private float fps = 60;
	
	/** Gestion des entités */
	private EntitiesManager entitiesManager = new EntitiesManager();
	
	/** gestion des niveaux */
	private LevelManager levelManager = new LevelManager(entitiesManager);
	
	/** The entity representing the player */
	private Entity ship;
	/** The speed at which the player's ship should move (pixels/sec) */
	private double moveSpeed = 300;
	/** The time at which last fired a shot */
	private long lastFire = 0;
	/** The interval between our players shot (ms) */
	private long firingInterval = 200;
	
	/** The message to display which waiting for a key press */
	private String message = "";
	/** True if we're holding up game play until a key has been pressed */
	private boolean waitingForKeyPress = true;
	/** True if the left cursor key is currently pressed */
	private boolean leftPressed = false;
	/** True if the right cursor key is currently pressed */
	private boolean rightPressed = false;
	/** True if we are firing */
	private boolean firePressed = false;
	
	/**
	 * Construct our game and set it running.
	 */
	public Game() {
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
		addKeyListener(new KeyInputHandler());
		
		// request the focus so key events come to us
		requestFocus();

		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics
		createBufferStrategy(2);
		bufferStrategy = getBufferStrategy();
		
		
		
		
		
		
		// initialise the entities in our game so there's something
		// to see at startup
		startLevel();
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
		
		// keep looping round til the game ends
		while (gameRunning) {
			long loop_start = System.nanoTime();
			long delta = (long) (1/fps*1000000000);
			
			handleEvent();
			
			updateLogic(delta);
			
            updateDisplay();
            
			try { Thread.sleep((delta-(System.nanoTime()-loop_start))/1000000); } catch (Exception e) {}
		}
	}
	
	
	
	

	
	
	private void handleEvent()
	{
		// resolve the movement of the ship. First assume the ship 
		// isn't moving. If either cursor key is pressed then
		// update the movement appropraitely
		
		if ((leftPressed) && (!rightPressed)) {
			ship.setHorizontalMovement(-moveSpeed);
		} else if ((rightPressed) && (!leftPressed)) {
			ship.setHorizontalMovement(moveSpeed);
		}
		
		// if we're pressing fire, attempt to fire
		if (firePressed) {
			tryToFire();
		}
	}
	
	
	
	private void updateLogic(long delta) {
		if (!waitingForKeyPress)
		{
			//Déplacer les entités
			entitiesManager.moveEntities(delta);

			//Si les entités rencontre un bord ou atteingnent le bas de l'écran 
			//(comportement modifiable dans les classes
			//du package entity)
			entitiesManager.doEntitiesLogic();
			
			//Vérifier si il y a eu des collisions
			//Supprimer les entités tués
			entitiesManager.doCollisions();
			
			// réinitialiser le déplacement du vaisseau
			ship.setHorizontalMovement(0);
		}
		
	}
	
	
	private void updateDisplay() {
		

		
		// Get hold of a graphics context for the accelerated 
		// surface and blank it out
		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0,0,800,600);

		//Afficher les entités
        entitiesManager.draw(g);
		
		// if we're waiting for an "any key" press then draw the 
		// current message 
		if (waitingForKeyPress) {
			g.setColor(Color.white);
			g.drawString(message,(800-g.getFontMetrics().stringWidth(message))/2,250);
			g.drawString("Press any key",(800-g.getFontMetrics().stringWidth("Press any key"))/2,300);
		}
		
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
		// create the player ship and place it roughly in the center of the screen
		ship = new EntityShip(this,"sprites/ship.gif",370,550,entitiesManager);
		entitiesManager.getEntitiesList().add(ship);
		
		// create block of aliens, with the arguments
		
		entitiesManager.getEntitiesList().addAll(levelManager.getCurrentLevel().generateLevel());
		
		// blank out any keyboard settings we might currently have
		leftPressed = false;
		rightPressed = false;
		firePressed = false;
	}
	
	/**
	 * Notification that the player has died. 
	 */
	public void notifyDeath() {
		message = "Oh no! They got you, try again?";
		levelManager.goToFirstLevel();
		waitingForKeyPress = true;
	}
	
	/**
	 * Notification that the player has won since all the aliens
	 * are dead.
	 */
	public void notifyWin() {
		message = "Well done! You Win!";
		levelManager.goToNextLevel();
		waitingForKeyPress = true;
	}
	
	/**
	 * Notification that an alien has been killed
	 */
	public void notifyAlienKilled() {
		
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
	
	
	
	/**
	 * Attempt to fire a shot from the player. Its called "try"
	 * since we must first check that the player can fire at this 
	 * point, i.e. has he/she waited long enough between shots
	 */
	public void tryToFire() {
		// check that we have waiting long enough to fire
		if (System.currentTimeMillis() - lastFire < firingInterval) {
			return;
		}
		
		// if we waited long enough, create the shot entity, and record the time.
		lastFire = System.currentTimeMillis();
		EntityShot shot = new EntityShotFromAlly(this,"sprites/shot.gif",ship.getX()+10,ship.getY()-30, 1, entitiesManager);
		entitiesManager.getEntitiesList().add(shot);
	}
	/**
	 * A class to handle keyboard input from the user. The class
	 * handles both dynamic input during game play, i.e. left/right 
	 * and shoot, and more static type input (i.e. press any key to
	 * continue)
	 * 
	 * This has been implemented as an inner class more through 
	 * habbit then anything else. Its perfectly normal to implement
	 * this as seperate class if slight less convienient.
	 * 
	 * @author Kevin Glass
	 */
	private class KeyInputHandler extends KeyAdapter {
		/** The number of key presses we've had while waiting for an "any key" press */
		private int pressCount = 1;
		
		/**
		 * Notification from AWT that a key has been pressed. Note that
		 * a key being pressed is equal to being pushed down but *NOT*
		 * released. Thats where keyTyped() comes in.
		 *
		 * @param e The details of the key that was pressed 
		 */
		public void keyPressed(KeyEvent e) {
			// if we're waiting for an "any key" typed then we don't 
			// want to do anything with just a "press"
			if (waitingForKeyPress) {
				return;
			}
			
			
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = true;
			}
		} 
		
		/**
		 * Notification from AWT that a key has been released.
		 *
		 * @param e The details of the key that was released 
		 */
		public void keyReleased(KeyEvent e) {
			// if we're waiting for an "any key" typed then we don't 
			// want to do anything with just a "released"
			if (waitingForKeyPress) {
				return;
			}
			
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = false;
			}
		}

		/**
		 * Notification from AWT that a key has been typed. Note that
		 * typing a key means to both press and then release it.
		 *
		 * @param e The details of the key that was typed. 
		 */
		public void keyTyped(KeyEvent e) {
			// if we're waiting for a "any key" type then
			// check if we've recieved any recently. We may
			// have had a keyType() event from the user releasing
			// the shoot or move keys, hence the use of the "pressCount"
			// counter.
			if (waitingForKeyPress) {
				if (pressCount == 1) {
					// since we've now recieved our key typed
					// event we can mark it as such and start 
					// our new game
					waitingForKeyPress = false;
					startLevel();
					pressCount = 0;
				} else {
					pressCount++;
				}
			}
			
			// if we hit escape, then quit the game
			if (e.getKeyChar() == 27) {
				System.exit(0);
			}
		}
	}
	
	
	
	
	
	
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
