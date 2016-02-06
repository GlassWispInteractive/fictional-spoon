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
				if (i + j <= 12) {
					map[i][j].setValue(1);
				}
			}
		}
	}

	public int getValue(int i, int j) {
		// TODO Auto-generated method stub
		return map[j][i].getValue();
	}
	
	

}
