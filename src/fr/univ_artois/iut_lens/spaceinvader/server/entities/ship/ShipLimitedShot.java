package fr.univ_artois.iut_lens.spaceinvader.server.entities.ship;

public interface ShipLimitedShot {
	
	public int getNbShotAlive();
	
	public int getMaxNbShot();
	
	public void addAliveShot();
	
	public void removeAliveShot();
	

}
