package dungeon;

import static dungeon.Ground.*;

import java.awt.Color;
import java.awt.Graphics2D;

public class Map {
	
	//drawing
	final int offsetX = 0, offsetY = 0;
	int fieldSize = 5, padding = 0, rectSize = fieldSize - padding;
	private Color[] color = { Color.decode("#454545"), Color.decode("#A1D490"), Color.decode("#D4B790"),
			Color.decode("#B39B7B"), Color.decode("#801B1B"), Color.decode("#000000") };
	
	
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
				map[i][j].setGround(WALL);
			}
		}
	}
	
	/**
	 * getter method for length n
	 * @return n
	 */
	public int getN() {
		return n;
	}
	
	/**
	 * getter method for length m
	 * @return m
	 */
	public int getM() {
		return m;
	}
	
	/**
	 * setter method for cell
	 * @param x coordinate
	 * @param y coordinate
	 * @return
	 */
	public Cell getCell(int x, int y) {
		return map[x][y];
	}
	
	/**
	 * getther method for ground state
	 * @param x coordinate
	 * @param y coordinate
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
	 * setter method for a new room on the map object
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
	
	
		
	public void draw(Graphics2D g2){
		for (int i = 0; i < this.getN(); i++) {
			int x = offsetY + i * fieldSize;
			for (int j = 0; j < this.getM(); j++) {
				int y = offsetX + j * fieldSize;
				g2.setColor(color[this.getGround(i, j).ordinal()]);
				g2.fillRect(x, y, rectSize, rectSize);
			}
		}
	}

}
