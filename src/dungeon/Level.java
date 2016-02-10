package dungeon;

public class Level {
	public int n, m;
	private Pixel[][] map;

	public Level(int n, int m) {
		this.n = n;
		this.m = m;

		map = new Pixel[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				map[i][j] = new Pixel(i, j);
				map[i][j].setValue(0);
			}
		}
	}

	public int getValue(int x, int y) {
		if (x < 0 || x >= n || y < 0 || y >= m) {
			return -1;
		}

		return map[x][y].getValue();
	}

	public boolean isWall(int x, int y) {
		return getValue(x, y) == 0;
	}
	
	public boolean isGround(int x, int y) {
		return getValue(x, y) > 0;
	}


	public boolean isFloor(int x, int y) {
		return getValue(x, y) % 10 == 1;
	}

	public boolean isRoom(int x, int y) {
		return getValue(x, y) % 10 == 2;
	}

	public void setValue(int i, int j, int val) {
		if (i < 0 || i >= n || j < 0 || j >= m) {
			return;
		}

		map[i][j].setValue(val);
	}

	public void fillSpace(int xMin, int xMax, int yMin, int yMax, int val) {
		for (int i = xMin; i < xMax; i++) {
			for (int j = yMin; j < yMax; j++) {
				map[i][j].setValue(val);
			}
		}
	}

}
