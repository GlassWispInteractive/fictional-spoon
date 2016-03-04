package gen;

import static gen.environment.Ground.*;

import java.util.*;

import gen.environment.Cell;
import gen.environment.DisjointSet;
import gen.environment.Map;

/**
 * class to generate level maps source:
 * http://journal.stuffwithstuff.com/2014/12/21/rooms-and-mazes/
 * 
 * @author Danny
 *
 */
public class Generator {
	// class variables

	private Random rand;
	private DisjointSet<Cell> cc; // connected components
	private Map map;
	private int n, m;
	private int roomTable[][], roomNum = 0;

	// constants

	final int ROOM_LIMIT = 300;
	final int[][] neighsAll = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
	final int[][] neighsOdd = new int[][] { { 2, 0 }, { -2, 0 }, { 0, 2 }, { 0, -2 } };

	/**
	 * constructor for generate levels with some fixed size
	 */
	public Generator(int n, int m) {
		// set size params
		this.n = n - (n + 1) % 2;
		this.m = m - (m + 1) % 2;

		// init pseudorandom generators
		rand = new Random();
		rand.setSeed(42);

		// init room super array
		roomTable = new int[ROOM_LIMIT][];
	}

	/**
	 * generating functions
	 * 
	 * @return random level
	 */
	public Map newLevel() {
		map = new Map(n, m);

		// 4 general steps to level generation
		cc = new DisjointSet<>();
		placeRooms();
		placeMaze();
		removeDeadends();

		cc = new DisjointSet<>();
		placeLoops();
		placeMaze();
		removeDeadends();

		return map;
	}

	/**
	 * internal function to generate a room and place it into the level
	 * 
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 */
	private void placeRooms() {
		// declare vars
		int xLen, yLen, xStart, yStart;

		// try to put up a new room in each iteration
		for (int i = 0; i < ROOM_LIMIT; i++) {
			// make sure to have valid room sizes
			// random num n transforms into 2n+1 -> odd
			do {
				xLen = (int) (4 + 2 * rand.nextGaussian());
				xLen = 2 * xLen + 1;
				yLen = (int) (4 + 2 * rand.nextGaussian());
				yLen = 2 * yLen + 1;
			} while (xLen < 4 || yLen < 4);

			// gen a position in the level
			// increment number if its even -> odd
			xStart = rand.nextInt(n - xLen);
			xStart = xStart + (xStart + 1) % 2;
			yStart = rand.nextInt(m - yLen);
			yStart = yStart + (yStart + 1) % 2;

			// check whether the position is valid
			if (!checkRoom(xStart, yStart, xLen, yLen)) {
				continue;
			}

			// place room
			map.setNewRoom(xStart, xLen, yStart, yLen);

			// insert room into memory
			roomTable[roomNum] = new int[] { xStart, xLen, yStart, yLen };
			roomNum++;

			// create connected compontents
			cc.makeSet(map.getCell(xStart, yStart));
			for (int x = xStart; x < xStart + xLen; x += 2) {
				for (int y = yStart; y < yStart + yLen; y += 2) {
					if (x == xStart && y == yStart)
						continue;

					cc.makeSet(map.getCell(x, y));
					cc.union(map.getCell(xStart, yStart), map.getCell(x, y));
				}
			}
		}
	}

	/**
	 * helper function to check for valid room positions
	 * 
	 * @param xStart
	 * @param yStart
	 * @param xLen
	 * @param yLen
	 * @return
	 */
	private boolean checkRoom(int xStart, int yStart, int xLen, int yLen) {
		// be sure to only check for odd numbers (xStart, yStart are odd)
		for (int i = 0; i <= xLen; i += 2) {
			for (int j = 0; j <= yLen; j += 2) {

				if (map.getGround(xStart + i, yStart + j) != WALL) {
					return false;
				}
			}
		}

		// no collision -> valid placement
		return true;
	}

	/**
	 * internal function to generate a maze around the rooms this is done by a
	 * floodfill algorithm instead of some overengineering with MST
	 */
	private void placeMaze() {
		ArrayList<int[]> q = new ArrayList<>();

		for (int i = 1; i < n; i += 2) {
			for (int j = 1; j < m; j += 2) {
				// fill in fixed cells on odd / odd coordinates
				if (map.getGround(i, j) == WALL) {
					map.setGround(i, j, FLOOR);
					cc.makeSet(map.getCell(i, j));
				}

				// queue neighbours when one corner is not a room
				if (i + 2 < n && map.getGround(i + 1, j) != ROOM)
					q.add(new int[] { i, j, i + 2, j });
				if (j + 2 < m && map.getGround(i, j + 1) != ROOM)
					q.add(new int[] { i, j, i, j + 2 });
			}
		}

		// choose connector in a random order
		noobShuffle(q);

		for (int[] e : q) {
			// rename array
			final int x1 = e[0], y1 = e[1], x2 = e[2], y2 = e[3];

			if (cc.findSet(map.getCell(x1, y1)) == null)
				continue;

			if (cc.findSet(map.getCell(x2, y2)) == null)
				continue;

			// check if two cells are already connected
			if (cc.findSet(map.getCell(x1, y1)) == cc.findSet(map.getCell(x2, y2)))
				continue;

			// merge two components by adding a connector
			cc.union(map.getCell(x1, y1), map.getCell(x2, y2));
			map.setGround((x1 + x2) / 2, (y1 + y2) / 2, FLOOR);
		}
	}

	/**
	 * internal function to remove the dead ends of the maze
	 */
	private void removeDeadends() {
		int count;
		boolean repeat = true, deadend[][];

		while (repeat) {
			// fresh inits for single execution of elemination
			deadend = new boolean[n][m];
			repeat = false;

			// dead end iff 3 neighbours are walls
			for (int x = 0; x < n; x++) {
				for (int y = 0; y < m; y++) {
					if (map.getGround(x, y) == WALL) {
						continue;
					}

					count = 0;
					for (int j = 0; j < 4; j++) {
						if (map.getGround(x + neighsAll[j][0], y + neighsAll[j][1]) == WALL)
							count++;
					}

					if (count >= 3) {
						deadend[x][y] = true;
						repeat = true;
					}
				}
			}

			// remove dead ends
			for (int x = 0; x < n; x++) {
				for (int y = 0; y < m; y++) {
					if (deadend[x][y])
						map.setGround(x, y, WALL);
				}
			}
		}
	}

	/**
	 * internal function to create loops inside the map
	 */
	private void placeLoops() {
		for (int i = 0; i < roomNum; i++) {
			// declare variables
			final int xStart = roomTable[i][0], xLen = roomTable[i][1], yStart = roomTable[i][2],
					yLen = roomTable[i][3];

			// check if room only has one floor connection
			int count = 0;
			for (int x = 0; x < xLen; x++) {
				if (map.getGround(xStart + x, yStart - 1) == FLOOR)
					count++;

				if (map.getGround(xStart + x, yStart + yLen) == FLOOR)
					count++;
			}

			for (int y = 0; y < yLen; y++) {
				if (map.getGround(xStart - 1, yStart + y) == FLOOR)
					count++;

				if (map.getGround(xStart + xLen, yStart + y) == FLOOR)
					count++;
			}

			if (count > 1) {
				continue;
			}

			// create a connected component for each room
			cc.makeSet(map.getCell(xStart, yStart));
			for (int x = xStart; x < xStart + xLen; x += 2) {
				for (int y = yStart; y < yStart + yLen; y += 2) {
					if (x == xStart && y == yStart)
						continue;

					cc.makeSet(map.getCell(x, y));
					cc.union(map.getCell(xStart, yStart), map.getCell(x, y));
				}
			}
		}

	}

	private void noobShuffle(ArrayList<int[]> q) {
		int i = q.size(), j, temp[];

		while (i > 1) {
			j = rand.nextInt(i);
			i--;

			temp = q.get(j);
			q.set(j, q.get(i));
			q.set(i, temp);
		}
	}

}
