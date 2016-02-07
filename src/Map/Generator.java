package Map;

import java.util.Random;
import java.util.Stack;

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
	private Stack<int[]> rooms;

	/**
	 * constructor for generate levels with some fixed size
	 */
	public Generator(int n, int m) {
		// set size params
		this.n = n;
		this.m = m;

		// init pseudorandom generators
		rand = new Random();

		this.rooms = new Stack<int[]>();
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
		connectRooms();
		removeDeadends();

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
			lvl.fillSpace(xStart, xStart + xLen, yStart, yStart + yLen);

			// push coords onto stack
			rooms.push(new int[] { xStart, xLen, yStart, yLen });
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
		// declarations
		Stack<int[]> s = new Stack<int[]>();
		int[] cur, old, perm;
		// int value;

		// push every tile on stack
		for (int i = 1; i < n; i += 2) {
			for (int j = 1; j < m; j += 2) {
				s.push(new int[] { i, j });
			}
		}

		old = s.peek();
		while (!s.isEmpty()) {
			cur = s.pop();

			if (lvl.getValue(cur[0], cur[1]) != 0) {
				continue;
			}

			// randomly push all four neighbours on the stack
			perm = fourPermutation();
			int[][] neigh = oddNeighbours(cur[0], cur[1]);

			for (int i = 0; i < 4; i++) {
				s.push(new int[] { neigh[perm[i]][0], neigh[perm[i]][1] });
			}

			// set current pixels
			lvl.setValue(cur[0], cur[1], 1);

			// set pixel in between, if last pixel is a neighbour
			if ((cur[0] == old[0] && Math.abs(cur[1] - old[1]) == 2)
					|| (cur[1] == old[1] && Math.abs(cur[0] - old[0]) == 2)) {
				lvl.setValue((cur[0] + old[0]) / 2, (cur[1] + old[1]) / 2, 1);
			}

			// renew old reference
			old = cur.clone();
		}
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
	private void connectRooms() {
		int[] cur;

		while (!rooms.isEmpty()) {
			cur = rooms.pop();
			connectRoom(cur[0], cur[1], cur[2], cur[3]);
		}
	}

	/**
	 * returns the border cells of a room
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
			if (lvl.getValue(xStart + i, yStart - 2) == 1) {
				candidates[k][0] = xStart + i;
				candidates[k][1] = yStart - 1;
				k++;
			}

			if (lvl.getValue(xStart + i, yStart + yLen + 1) == 1) {
				candidates[k][0] = xStart + i;
				candidates[k][1] = yStart + yLen;
				k++;
			}
			// full debug vis
			// lvl.setValue(xStart + i, yStart - 1, 3);
			// lvl.setValue(xStart + i, yStart + yLen, 3);
		}

		// connector from vertical borders
		for (int i = 0; i < yLen; i++) {
			if (lvl.getValue(xStart - 2, yStart + i) == 1) {
				candidates[k][0] = xStart - 1;
				candidates[k][1] = yStart + i;
				k++;
			}

			if (lvl.getValue(xStart + xLen + 1, yStart + i) == 1) {
				candidates[k][0] = xStart + xLen;
				candidates[k][1] = yStart + i;
				k++;
			}

			// full debug vis
			// lvl.setValue(xStart - 1 , yStart + i, 3);
			// lvl.setValue(xStart + xLen, yStart + i, 3);
		}

		// create new connectors, at least 1 at most k
		do {
			int l = rand.nextInt(k);
			lvl.setValue(candidates[l][0], candidates[l][1], 3);
		} while (rand.nextDouble() < 0.2);
	}

	/**
	 * internal function to remove the dead ends of the maze
	 */
	private void removeDeadends() {

	}

}
