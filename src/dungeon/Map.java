package dungeon;

import static dungeon.Ground.*;

public class Map {
	private Cell[][] map;
	private int n, m;
	
	/**
	 * public constructor
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
		
		for (int i = 1; i < n; i += 2) {
			for (int j = 1; j < m; j += 2) {
				map[i][j].set(FLOOR);
			}
		}
	}
	
	
	
}
