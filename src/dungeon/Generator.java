package dungeon;

import java.util.*;

/**
 * class to generate level maps source:
 * http://journal.stuffwithstuff.com/2014/12/21/rooms-and-mazes/
 * 
 * @author Danny
 *
 */
public class Generator {
	private Level lvl;
	private int n, m;

	private Random rand;

	final int ROOM_LIMIT = 500;
	private int roomTable[][], roomNum = 0;

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
	public Level newLevel() {
		lvl = new Level(n, m);

		// 4 general steps to level generation
		placeRooms();
		placeMaze();
		connectCells();
		removeDeadends(5);

		return lvl;
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
		for (int i = 0; i < 500; i++) {
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
			lvl.fillSpace(xStart, xStart + xLen, yStart, yStart + yLen, 10 * roomNum + 2);

			// insert room
			roomTable[roomNum] = new int[] { xStart, xLen, yStart, yLen };
			roomNum++;
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

				if (lvl.getValue(xStart + i, yStart + j) != 0) {
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
		// push every tile on stack
		for (int i = 1; i < n; i += 2) {
			for (int j = 1; j < m; j += 2) {
				floodfill(i, j, 1);
			}
		}
	}

	private void floodfill(int x, int y, int count) {
		// fast break condition
		if (lvl.getValue(x, y) != 0) {
			return;
		}

		// here it begins
		Stack<int[]> s = new Stack<int[]>();

		s.push(new int[] { x, y });

		while (!s.isEmpty()) {
			// consider pixel (x, y)
			x = s.peek()[0];
			y = s.peek()[1];
			s.pop();

			// skip if pixel is already set
			if (lvl.getValue(x, y) != 0) {
				continue;
			}

			// randomly push all four neighbours on the stack
			int[] perm = fourPermutation();
			int[][] neigh = oddNeighbours(x, y);

			int oldnum = 0;
			for (int i = 0; i < 4; i++) {
				if (lvl.getValue(neigh[perm[i]][0], neigh[perm[i]][1]) == 10 * count + 1) {
					oldnum += 1;
				}
			}
			if (oldnum > 1) {
				continue;
			}

			for (int i = 0; i < 4; i++) {
				s.push(new int[] { neigh[perm[i]][0], neigh[perm[i]][1] });
			}

			lvl.setValue(x, y, 10 * count + 1);
		}

		// s.pop();

	}

	/**
	 * helper function to determine the neighbours of odd cells
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private int[][] oddNeighbours(int x, int y) {
		int[][] neigh = new int[][] { { x + 2, y }, { x - 2, y }, { x, y + 2 }, { x, y - 2 } };
		return neigh;
	}

	/**
	 * helper function to determine the neighbours of cells (even counted in)
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private int[][] allNeighbours(int x, int y) {
		int[][] neigh = new int[][] { { x + 1, y }, { x - 1, y }, { x, y + 1 }, { x, y - 1 } };
		return neigh;
	}

	/**
	 * helper function for the algorithm of place maze
	 * 
	 * @return permutation of array [1, 2, 3, 4]
	 */
	private int[] fourPermutation() {
		int[] perm = { 0, 1, 2, 3 };
		int r, t;

		for (int i = 3; i > -1; i--) {
			r = rand.nextInt(i + 1);
			t = perm[r];
			perm[r] = perm[i];
			perm[i] = t;
		}

		return perm;
	}

	/**
	 * reimplementation of fourPermutation (slightly different since list is 1,
	 * 2, 3, 4)
	 * 
	 * @return permutation of 4 values
	 */
	@SuppressWarnings("unused")
	private int[] fourBrainfuck() {
		int[] perm = new int[4];
		int n = rand.nextInt(24);

		// trivial case
		perm[0] = (n % 4) + 1;

		// almost trival
		perm[1] = (n % 3) + 1;
		if (perm[1] >= perm[0])
			perm[1]++;

		// total nontrivial
		perm[2] = 10 - perm[0] - perm[1];
		if (perm[2] == 5) {
			perm[2] = 2 + (2 & perm[0]);
		} else if (perm[2] > 5) {
			perm[2] = 4;
		} else {
			perm[2] = 1;
		}

		// swap pos 3 and 4 based on "first" bit
		if (n / 12 == 0) {
			perm[3] = perm[2];
			perm[2] = 10 - perm[0] - perm[1] - perm[3];
		} else {
			perm[3] = 10 - perm[0] - perm[1] - perm[2];
		}

		return perm;
	}

	/**
	 * internal function to connect the rooms through the maze
	 */
	private void connectCells() {
		// fill in horizontal connectors
		for (int i = 0; i < n; i += 2) {
			for (int j = 1; j < m; j += 2) {
				if (lvl.getValue(i - 1, j) == lvl.getValue(i + 1, j)) {
					lvl.setValue(i, j, lvl.getValue(i - 1, j));
				}

			}
		}

		// // fill in vertical connectors
		for (int i = 1; i < n; i += 2) {
			for (int j = 0; j < m; j += 2) {
				if (lvl.getValue(i, j - 1) == lvl.getValue(i, j + 1)) {
					lvl.setValue(i, j, lvl.getValue(i, j - 1));
				}
			}
		}

		for (int i = 0; i < roomNum; i++) {
			connectRoom(roomTable[i][0], roomTable[i][1], roomTable[i][2], roomTable[i][3]);
		}

		// Stack<int[]> s = new Stack<int[]>();

		// for (int i = 0; i < n; i += 2) {
		// for (int j = 0; j < m; j += 2) {
		// if (lvl.getValue(i, j) == 1) {
		// s.push(new int[] { i, j });
		// }
		// }
		// }

		// int[] cur;
		//
		// for (int i = 0; i < roomNum; i++) {
		// cur = roomTable[i];
		// connectRoom(cur[0], cur[1], cur[2], cur[3]);
		// }

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
	private void connectRoom(int xStart, int xLen, int yStart, int yLen) {
		// declarations
		int k = 0, candidates[][] = new int[(xLen + 2) * (yLen + 2)][2];

		// connector from horizontal borders
		for (int i = 0; i < xLen; i++) {
			if (lvl.isFloor(xStart + i, yStart - 2)) {
				candidates[k][0] = xStart + i;
				candidates[k][1] = yStart - 1;
				k++;
			}

			if (lvl.isFloor(xStart + i, yStart + yLen + 1)) {
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
			if (lvl.isFloor(xStart - 2, yStart + i)) {
				candidates[k][0] = xStart - 1;
				candidates[k][1] = yStart + i;
				k++;
			}

			if (lvl.isFloor(xStart + xLen + 1, yStart + i)) {
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
			lvl.setValue(candidates[l][0], candidates[l][1], 3);
		} while (rand.nextDouble() < 0.3);
	}

	/**
	 * internal function to remove the dead ends of the maze
	 */
	private void removeDeadends(int iter) {
		// declarations
		Queue<int[]> q = new ArrayDeque<int[]>();
		int[] cur, perm;
		int[][] neigh;
		// int value;

		// push every cell on stack
		// because connectors cannot be a dead end
		for (int i = 0; i < n; i += 1) {
			for (int j = 0; j < m; j += 1) {
				if (lvl.getValue(i, j) > 1) {
					q.add(new int[] { i, j });
				}
			}
		}
		q.add(new int[] { -1, -1 });

		// remove if dead end and push the neighbour on stack which might be a
		// dead end now
		while (!q.isEmpty()) {
			cur = q.poll();
			neigh = allNeighbours(cur[0], cur[1]);

		}

	}

}
