package fr.univ_artois.iut_lens.spaceinvader.entities.ship;

public interface ShipLimitedShot {
	
	public int getNbShotAlive();
	
	public int getMaxNbShot();
	
	public void addAliveShot();
	
	public void removeAliveShot();
	

}
