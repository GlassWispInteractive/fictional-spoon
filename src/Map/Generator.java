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
//		randBool.setSeed(42);
		randGauss = new Random();
//		randGauss.setSeed(1337);
	}
	
	
	/**
	 * generating functions
	 * @return random level
	 */
	public Level newLevel() {
		lvl = new Level(n, m);
		split(0, n, 0, m, 20);
		
		return lvl;
	}
	
	private void split(int xMin, int xMax, int yMin, int yMax, int Iter) {
		// leave as space when too small
		if (xMax - xMin < 4 || yMax - yMin < 4) {
			return;
		}
		
		
		
		
		// create random object
		
		double x = 0.5 + randGauss.nextGaussian() / 6;
		System.out.println(x);
		
		System.out.println(randBool.nextBoolean());
	}
}
