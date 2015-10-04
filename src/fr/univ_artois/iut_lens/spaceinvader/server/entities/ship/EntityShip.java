package fr.univ_artois.iut_lens.spaceinvader.server.entities.ship;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.client.PacketClientCommand.Direction;
import fr.univ_artois.iut_lens.spaceinvader.server.EntitiesManager;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.ennemy.EntityEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.server.entities.shot.EntityShotFromEnnemy;
import fr.univ_artois.iut_lens.spaceinvader.server.players.ShipManager;
import fr.univ_artois.iut_lens.spaceinvader.sprites_manager.SpriteStore;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

/**
 * The entity that represents the players ship
 * 
 * @author Kevin Glass
 */
public abstract class EntityShip extends Entity {
	protected long lastFireTime; //Dernier tir du vaisseau
	private long fireInterval; //Intervalle de temps par défaut pour lequel le vaisseau peut tirer


	private double moveSpeed; // Vitesse de déplacement du vaisseau
	
	public final ShipManager associatedShipManager;

	
	/**
	 * Create a new entity to represent the players ship
	 *  
	 * @param gameInstance The game in which the ship is being created
	 * @param ref The reference to the sprite to show for the ship
	 * @param x The initial x location of the player's ship
	 * @param y The initial y location of the player's ship
	 */
	public EntityShip(String ref, double speed, EntitiesManager eM, long fireInt, ShipManager sm) {
		super(ref,
				new Vector2d(
						MegaSpaceInvader.DISPLAY_WIDTH/2-SpriteStore.get().getSprite(ref).getWidth()/2,	// permet de centrer l'image
						MegaSpaceInvader.DISPLAY_HEIGHT-SpriteStore.get().getSprite(ref).getHeight()),
				eM);
		fireInterval = fireInt;
		moveSpeed = speed;
		associatedShipManager = sm;
		reinitShot();
	}
	
	/**
	 * Request that the ship move itself based on an elapsed ammount of
	 * time
	 * 
	 * @param delta The time that has elapsed since last move (ms)
	 */
	public void move(long delta) {
		// if we're moving left and have reached the left hand side
		// of the screen, don't move
		if ((speed.x < 0) && (position.x < 0)) {
			setHorizontalDirection(Direction.NONE);
			return;
		}
		// if we're moving right and have reached the right hand side
		// of the screen, don't move
		if ((speed.x > 0) && (position.x > MegaSpaceInvader.DISPLAY_WIDTH - sprite.getWidth())) {
			setHorizontalDirection(Direction.NONE);
			return;
		}
		
		super.move(delta);
		setHorizontalDirection(Direction.NONE);
	}
	
	/**
	 * Notification that the player's ship has collided with something
	 * 
	 * @param other The entity with which the ship has collided
	 */
	public synchronized void collidedWith(Entity other) {
		// if its an alien or a shoot, notify the game that the player
		// is dead
		if (other instanceof EntityEnnemy || other instanceof EntityShotFromEnnemy) {
			associatedShipManager.getPlayer().die();
		}
	}
	
	/**
	 * 
	 * @param command vaut -1 (gauche), 0 (stop) ou 1 (droite)
	 */
	public void setHorizontalDirection(Direction dir)
	{
		if (dir == Direction.NONE)
			speed.x = 0;
		if (dir == Direction.LEFT)
			speed.x = -moveSpeed;
		if (dir == Direction.RIGHT)
			speed.x = moveSpeed;
	}
	
	public void setPosition(Vector2d pos) {
		position = pos;
	}
	

	@Override
	public Camp getCamp() {
		return Camp.ALLY;
	}
	

	public void tryToShoot(long currentTime) {
		if (!canShoot(currentTime)) return;
		lastFireTime = currentTime; //On met le dernier tire au temps actuel
		shoot(currentTime);
	}
	
	protected boolean canShoot(long currentTime) {
		if(currentTime - lastFireTime < fireInterval) return false; //L'interval de tir est trop court
		return true;
	}
	
	protected abstract void shoot(long currentTime);
	
	
	
	public void reinitShot() {
		lastFireTime = Long.MIN_VALUE/2;
	}
	
}