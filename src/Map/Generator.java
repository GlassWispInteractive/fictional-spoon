package Map;

import java.util.Random;

/**
 * class to generate level maps
 * source: http://journal.stuffwithstuff.com/2014/12/21/rooms-and-mazes/
 * @author Danny
 *
 */
public class Generator {
	private int n, m;
	private Level lvl;

	private Random rand;

	/**
	 * constructor for generate levels with some fixed size
	 */
	public Generator(int n, int m) {
		// set size params
		this.n = n;
		this.m = m;

		// init pseudorandom generators
		rand = new Random();
	}

	/**
	 * generating functions
	 * 
	 * @return random level
	 */
	public Level newLevel() {
		lvl = new Level(n, m);
		
//		4 general steps to level generation 
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
	 * internal function to generate a maze around the rooms
	 */
	private void placeMaze() {

	}

	/**
	 * internal function to connect the rooms through the maze
	 */
	private void connectRooms() {

	}

	/**
	 * internal function to remove the dead ends of the maze
	 */
	private void removeDeadends() {

	}

}
