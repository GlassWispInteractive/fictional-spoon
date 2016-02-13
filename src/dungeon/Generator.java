package dungeon;

import static dungeon.Ground.*;

import java.util.*;

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
	private DisjointSet<Cell> cc;
	private Map map;
	private int n, m;
	private int roomTable[][], roomNum = 0;

	// constants

	final int ROOM_LIMIT = 300;
	final int[][] neighsAll = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, 1 } };
	final int[][] neighsOdd = new int[][] { { 2, 0 }, { -2, 0 }, { 0, 2 }, { 0, 2 } };

	/**
	 * constructor for generate levels with some fixed size
	 */
	public Generator(int n, int m) {
		// set size params
		this.n = n;
		this.m = m;

		// init pseudorandom generators
		rand = new Random();

		// init room super array
		roomTable = new int[ROOM_LIMIT][];
	}

	/**
	 * generating functions
	 * 
	 * @return random level
	 */
	public Map newLevel() {
		cc = new DisjointSet<>();
		map = new Map(n, m);

		// 4 general steps to level generation
		placeRooms();
		placeMaze();
		removeDeadends(5);

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
			map.newRoom(xStart, xLen, yStart, yLen);

			// insert room into memory
			roomTable[roomNum] = new int[] { xStart, xLen, yStart, yLen };
			roomNum++;

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

		// fill in fixed cells on odd / odd coordinates
		for (int i = 1; i < n; i += 2) {
			for (int j = 1; j < m; j += 2) {
				if (map.getGround(i, j) == WALL) {
					map.setGround(i, j, FLOOR);
					cc.makeSet(map.getCell(i, j));

					// neighbours
					if (i + 2 < n)
						q.add(new int[] { i, j, i + 2, j });
					if (j + 2 < m)
						q.add(new int[] { i, j, i, j + 2 });
				}
			}
		}

		Collections.shuffle(q);

		// try every cell if it would connect two components into one
		for (int[] e : q) {
			// rename array
			final int x1 = e[0], y1 = e[1], x2 = e[2], y2 = e[3];

			if (cc.findSet(map.getCell(x1, y1)) == cc.findSet(map.getCell(x2, y2)))
				continue;

			cc.union(map.getCell(x1, y1), map.getCell(x2, y2));
			map.setGround((x1 + x2) / 2, (y1 + y2) / 2, FLOOR);
		}
	}

	/**
	 * connects border cells of a room with the maze
	 * 
	 * @param xStart
	 * @param yStart
	 * @param xLen
	 * @param yLen
	 * @return
	 */
	@SuppressWarnings("unused")
	private void connectRoom(int xStart, int xLen, int yStart, int yLen) {
		// declarations
		int k = 0, candidates[][] = new int[(xLen + 2) * (yLen + 2)][2];

		// connector from horizontal borders
		for (int i = 0; i < xLen; i++) {
			if (map.getGround(xStart + i, yStart - 2) == FLOOR) {
				candidates[k][0] = xStart + i;
				candidates[k][1] = yStart - 1;
				k++;
			}

			if (map.getGround(xStart + i, yStart + yLen + 1) == FLOOR) {
				candidates[k][0] = xStart + i;
				candidates[k][1] = yStart + yLen;
				k++;
			}
			// debug vis
			// lvl.setValue(xStart + i, yStart - 1, 3);
			// lvl.setValue(xStart + i, yStart + yLen, 3);
		}

		// connector from vertical borders
		for (int i = 0; i < yLen; i++) {
			if (map.getGround(xStart - 2, yStart + i) == FLOOR) {
				candidates[k][0] = xStart - 1;
				candidates[k][1] = yStart + i;
				k++;
			}

			if (map.getGround(xStart + xLen + 1, yStart + i) == FLOOR) {
				candidates[k][0] = xStart + xLen;
				candidates[k][1] = yStart + i;
				k++;
			}

			// debug vis
			// lvl.setValue(xStart - 1 , yStart + i, 3);
			// lvl.setValue(xStart + xLen, yStart + i, 3);
		}

		// create new connectors
		// 1 <= num <= k, expected value is around 1.3 per room
		do {
			if (k == 0) {
				// System.out.println("fail");
				break;
			}
			int l = rand.nextInt(k);
			map.setGround(candidates[l][0], candidates[l][1], CONNECTOR);
		} while (rand.nextDouble() < 0.3);
	}

	/**
	 * internal function to remove the dead ends of the maze
	 */
	@SuppressWarnings("unused")
	private void removeDeadends(int iter) {
		// declarations
		Queue<int[]> q = new ArrayDeque<int[]>();
		int cur[], perm[], neigh[][];

		// push every cell on stack
		// because connectors cannot be a dead end
		for (int i = 0; i < n; i += 1) {
			for (int j = 0; j < m; j += 1) {
				if (map.getGround(i, j) != WALL) {
					q.add(new int[] { i, j });
				}
			}
		}
		q.add(new int[] { -1, -1 });

		// remove if dead end and push the neighbour on stack which might be a
		// dead end now
		while (!q.isEmpty()) {
			cur = q.poll();

		}

	}

}
