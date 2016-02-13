package dungeon;

import static dungeon.Ground.*;

public class Map {
	private Cell[][] map;
	private int n, m;

	/**
	 * public constructor
	 * 
	 * @param n
	 * @param m
	 */
	public Map(int n, int m) {
		this.n = n;
		this.m = m;
		map = new Cell[n][m];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				map[i][j] = new Cell(i, j);
				map[i][j].set(WALL);
			}
		}
	}

	public int getN() {
		return n;
	}

	public int getM() {
		return m;
	}
	
	public Cell getCell(int x, int y) {
		return map[x][y];
	}

	public Ground getGround(int x, int y) {
		if (x < 0 || x >= n || y < 0 || y >= m) {
			return ERROR;
		}

		return map[x][y].get();
	}

	public void setGround(int x, int y, Ground ground) {
		if (x < 0 || x >= n || y < 0 || y >= m) {
			return;
		}

		map[x][y].set(ground);
	}

	public void newRoom(int xStart, int xLen, int yStart, int yLen) {
		for (int x = xStart; x < xStart + xLen; x++) {
			for (int y = yStart; y < yStart + yLen; y++) {
				map[x][y].set(ROOM);
			}
		}
	}

}
