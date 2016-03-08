package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class TileFactory {

	private static TileFactory singleton;

	// load tile sets
	private final Image[] TILE_SETS = new Image[] { new Image("/resources/roguelikeMap_transparent.png"),
			new Image("/resources/roguelikeIndoor_transparent.png"),
			new Image("/resources/roguelikeChar_transparent.png") };
	
	private Image[][][] subTiles;

	public static TileFactory getTilesFactory() {
		if (singleton == null) {
			singleton = new TileFactory();
		}
		return singleton;
	}

	private TileFactory() {
		
		int sourceCount = TILE_SETS.length;
		subTiles = new Image[sourceCount][][];
		
		//1st dim. count of different ressauces
		for(int i = 0; i < sourceCount; i++){
			//2. dim number of cols
			int cols = (int) ((TILE_SETS[i].getWidth() + 1) / 17);
			subTiles[i] = new Image[cols][];
			for(int j = 0; j < cols; j++){
				//3. dim number of rows
				int rows = (int) ((TILE_SETS[i].getHeight() + 1) / 17);
				subTiles[i][j] = new Image[rows];
			}
		}
	}
	
	/**
	 * 
	 * @param gc
	 * @param tileSource
	 * @param x , coordinate for drawing
	 * @param y , coordinate for drawing
	 * @param size , scaling size
	 * @param tileX , choose tile with coordinate (starting at 0, 0)
	 * @param tileY , choose tile with coordinate (starting at 0, 0)
	 */
	public void drawTile(GraphicsContext gc, TileSource tileSource, int x, int y, int size, int tileX, int tileY) {
		
		if(subTiles[tileSource.ordinal()][tileX][tileY] == null){
						
			PixelReader reader = TILE_SETS[tileSource.ordinal()].getPixelReader();
			WritableImage newImage = new WritableImage(reader, (16 + 1) * tileX, (16 + 1) * tileY, 16, 16);
			
			subTiles[tileSource.ordinal()][tileX][tileY] = newImage;
		}
		
		gc.drawImage(subTiles[tileSource.ordinal()][tileX][tileY], x*size, y*size);
	}
}
