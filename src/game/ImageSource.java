package game;

public class ImageSource {
	
	private TileSource tileSource;
	private int tileX;
	private int tileY;
	
	public ImageSource(TileSource tileSource, int tileX, int tileY) {
		this.tileSource = tileSource;
		this.tileX = tileX;
		this.tileY = tileY;
	}

	public int getTileY() {
		return tileY;
	}
	
	public int getTileX() {
		return tileX;
	}

	public int getTileSourceOrdinal() {
		return tileSource.ordinal();
	}
	
	

}
