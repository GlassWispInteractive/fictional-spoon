package dungeon;

public class Cell {
	private int x, y;
	private Ground ground;
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
		this.ground = Ground.WALL;
	}
	
	public void set(Ground ground) {
		this.ground = ground;
	}
	
	public Ground get() {
		return ground;
	}
}
