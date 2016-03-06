package gen.environment;

import static gen.environment.Ground.*;

public class Map {
	private Cell[][] map;
	private int[][] tileNumber;
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
		tileNumber = new int[n][m];

		for (int i = 0; i < n; i++) {
			tileNumber[i] = new int[m];
			for (int j = 0; j < m; j++) {
//				tileNumber[i] = new int[m][n];
				map[i][j] = new Cell(i, j);
				map[i][j].setGround(WALL);
			}
		}
	}

	/**
	 * getter method for length n
	 * 
	 * @return n
	 */
	public int getN() {
		return n;
	}

	/**
	 * getter method for length m
	 * 
	 * @return m
	 */
	public int getM() {
		return m;
	}

	/**
	 * setter method for cell
	 * 
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @return
	 */
	public Cell getCell(int x, int y) {
		return map[x][y];
	}

	/**
	 * getther method for ground state
	 * 
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @return
	 */
	public Ground getGround(int x, int y) {
		if (x < 0 || x >= n || y < 0 || y >= m) {
			return ERROR;
		}

		return map[x][y].getGround();
	}

	/**
	 * setter method for ground state
	 * 
	 * @param x
	 * @param y
	 * @param ground
	 */
	public void setGround(int x, int y, Ground ground) {
		if (x < 0 || x >= n || y < 0 || y >= m) {
			return;
		}

		map[x][y].setGround(ground);
	}

	/**
	 * @return the tileNumber
	 */
	public int getTileNumber(int x, int y) {
		if (x < 0 || x >= n || y < 0 || y >= m) {
			return -1;
		}
		
		return tileNumber[x][y];
	}

	/**
	 * @param tileNumber the tileNumber to set
	 */
	public void setTileNumber(int x, int y, int tile) {
		if (x < 0 || x >= n || y < 0 || y >= m) {
			return;
		}

		tileNumber[x][y] = tile;
	}

	/**
	 * setter method for a new room on the map object
	 * 
	 * @param xStart
	 * @param xLen
	 * @param yStart
	 * @param yLen
	 */
	public void setNewRoom(int xStart, int xLen, int yStart, int yLen) {
		for (int x = xStart; x < xStart + xLen; x++) {
			for (int y = yStart; y < yStart + yLen; y++) {
				map[x][y].setGround(ROOM);
			}
		}
	}

	public boolean isWalkable(int x, int y) {
		return getGround(x, y) != WALL && getGround(x, y) != ERROR;
	}

}