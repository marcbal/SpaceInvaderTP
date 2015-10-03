package fr.univ_artois.iut_lens.spaceinvader.network_packet.client;

public class PacketClientCommand extends PacketClient {
	
	public PacketClientCommand() {
		super(211);
		setData("--");
	}
	
	
	
	
	public Direction getDirection() {
		if (getData().charAt(0) == 'l')
			return Direction.LEFT;
		if (getData().charAt(0) == 'r')
			return Direction.RIGHT;
		return Direction.NONE;
	}
	
	
	
	public void setDirection(Direction dir) {
		char[] data = getData().toCharArray();
		switch(dir) {
		case LEFT:
			data[0] = 'l';
			break;
		case RIGHT:
			data[0] = 'r';
			break;
		case NONE:
			data[0] = '-';
			break;
			
		}
		
		setData(new String(data));
	}
	
	
	
	public static enum Direction {
		LEFT, NONE, RIGHT
	}
	
	
	
	public boolean isShooting() {
		return getData().charAt(1) == 's';
	}
	
	public void setShooting(boolean s) {
		char[] data = getData().toCharArray();
		data[1] = s ? 's' : '-';
		setData(new String(data));
	}
}
