package Map;

public class Level {
	public int n, m;
	private Pixel[][] map;
	
	public Level(int n, int m) {
		this.n = n;
		this.m = m;
		
		map = new Pixel[n][m];
		for(int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				map[i][j] = new Pixel(i, j);
				map[i][j].setValue(0);
				
				
//				if (i + j <= 40) {
//					map[i][j].setValue(1);
//				}
//				
//
//				if (i - j > 120) {
//					map[i][j].setValue(2);
//				}
//				
//				if (j- i > 100) {
//					map[i][j].setValue(3);
//				}
			}
		}
	}

	public int getValue(int i, int j) {
		if (i < 0 || i >= n || j < 0 || j >= m) {
			return -1;
		}
		
		return map[i][j].getValue();
	}
	
	public void setValue(int i, int j, int value) {
		if (i < 0 || i >= n || j < 0 || j >= m) {
			return;
		}
		
		map[i][j].setValue(value);
	}
	
	public void fillSpace(int xMin, int xMax, int yMin, int yMax) {
		for (int i = xMin; i < xMax; i++) {
			for (int j = yMin; j < yMax; j++) {
				map[i][j].setValue(1);
			}
		}
	}

}
