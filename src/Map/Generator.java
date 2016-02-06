package Map;

import java.util.Random;

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
		// randBool.setSeed(42);
		rand = new Random();
		// randGauss.setSeed(1337);
	}

	/**
	 * generating functions
	 * 
	 * @return random level
	 */
	public Level newLevel() {
		lvl = new Level(n, m);
		placeRooms(0, n, 0, m);

		return lvl;
	}

	private void placeRooms(int xMin, int xMax, int yMin, int yMax) {
		// declare vars
		int xLen, yLen, xStart, yStart;

		// try to put up a new room
		for (int i = 0; i < 500; i++) {
			do {
				xLen = (int) (8 + 2.5 * rand.nextGaussian());
				yLen = (int) (8 + 2.5 * rand.nextGaussian());
			} while (xLen < 4 || yLen < 4);

			xStart = rand.nextInt(n - xLen);
			yStart = rand.nextInt(m - yLen);

			if (!checkRoom(xStart, yStart, xLen, yLen)) {
				continue;
			}

			System.out.print(xLen);
			System.out.print(" - ");
			System.out.println(yLen);
			lvl.fillSpace(xStart, xStart + xLen, yStart, yStart + yLen);
		}

		// if (randBool.nextBoolean()) {
		// int split = (int) (xMin + splitRatio * (xMax - xMin));
		// splitSpace(xMin, split, yMin, yMax, iter - 1);
		// splitSpace(split+1, xMax, yMin, yMax, iter - 1);
		// } else {
		// int split = (int) (yMin + splitRatio * (yMax - yMin));
		// splitSpace(xMin, xMax, yMin, split, iter - 1);
		// splitSpace(xMin, xMax, split+1, yMax, iter - 1);
		// }

		// create random object

	}

	private boolean checkRoom(int xStart, int yStart, int xLen, int yLen) {
		for (int i = -1; i <= xLen; i++) {
			for (int j = -1; j <= yLen; j++) {

				if (lvl.getValue(xStart + i, yStart + j) != 0) {
					return false;
				}
			}
		}

		// no collision -> valid placement
		return true;
	}

}
