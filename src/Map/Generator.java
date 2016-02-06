package Map;

import java.util.Random;

public class Generator {
	private int n, m;
	private Level lvl;

	private Random randBool;
	private Random randGauss;

	/**
	 * constructor for generate levels with some fixed size
	 */
	public Generator(int n, int m) {
		// set size params
		this.n = n;
		this.m = m;

		// init pseudorandom generators
		randBool = new Random();
		// randBool.setSeed(42);
		randGauss = new Random();
		// randGauss.setSeed(1337);
	}

	/**
	 * generating functions
	 * 
	 * @return random level
	 */
	public Level newLevel() {
		lvl = new Level(n, m);
		splitSpace(0, n, 0, m, 10);

		return lvl;
	}

	private void splitSpace(int xMin, int xMax, int yMin, int yMax, int iter) {
		// System.out.print(xMin);
		// System.out.print(" - ");
		// System.out.print(xMax);
		// System.out.print(" - ");
		// System.out.print(yMin);
		// System.out.print(" - ");
		// System.out.println(yMax);

		// leave as space when too small
		if (xMax - xMin < 4 || yMax - yMin < 4) {
			return;
		}

		// iteration limit exceeded
		if ((xMax - xMin) * (yMax - yMin) < 40) {
			lvl.fillSpace(xMin, xMax, yMin, yMax);
			return;
		}

		// spliting line
		double splitRatio = 0.5 + randGauss.nextGaussian() / 6;

		if (randBool.nextBoolean()) {
			int split = (int) (xMin + splitRatio * (xMax - xMin));
			splitSpace(xMin, split, yMin, yMax, iter - 1);
			splitSpace(split+1, xMax, yMin, yMax, iter - 1);
		} else {
			int split = (int) (yMin + splitRatio * (yMax - yMin));
			splitSpace(xMin, xMax, yMin, split, iter - 1);
			splitSpace(xMin, xMax, split+1, yMax, iter - 1);

		}

		// create random object

	}

}
