package fr.univ_artois.iut_lens.spaceinvader.server.entities.ship;

import java.util.concurrent.atomic.AtomicInteger;

public interface ShipLimitedShot {
	
	public int getMaxNbShot();
	
	public abstract AtomicInteger getRefNbShotAlive();
	
	public default int getNbShotAlive() {
		return getRefNbShotAlive().get();
	}
	
	
	public default void addAliveShot() {
		getRefNbShotAlive().incrementAndGet();
	}
	
	public default void removeAliveShot() {
		getRefNbShotAlive().decrementAndGet();
	}
	

}
