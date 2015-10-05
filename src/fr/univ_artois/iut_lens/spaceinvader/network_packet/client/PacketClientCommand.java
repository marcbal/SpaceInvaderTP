package fr.univ_artois.iut_lens.spaceinvader.network_packet.client;

public class PacketClientCommand extends PacketClient {
	
	public PacketClientCommand() {
		super((byte)0x01);
		setData(new byte[] {0});
	}
	
	
	
	
	public Direction getDirection() {
		byte data = getData()[0];
		data >>= 4;
		if (data == 0x1)
			return Direction.LEFT;
		if (data == 0x2)
			return Direction.RIGHT;
		return Direction.NONE;
	}
	
	
	
	public void setDirection(Direction dir) {
		byte[] data = getData();
		data[0] &= 0x0F;
		switch(dir) {
		case LEFT:
			data[0] |= 0x10;
			break;
		case RIGHT:
			data[0] |= 0x20;
			break;
		case NONE:
			break;
		}
	}
	
	
	
	public static enum Direction {
		LEFT, NONE, RIGHT
	}
	
	
	
	public boolean isShooting() {
		return (getData()[0] & 0xF) == 0x1;
	}
	
	public void setShooting(boolean s) {
		byte[] data = getData();
		data[0] &= 0xF0;
		if (s)
			data[0] |= 0x01;
	}
}
