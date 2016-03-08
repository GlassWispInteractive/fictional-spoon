package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import static game.TileSource.*;

public class TilesFactory {

	private static TilesFactory singleton;

	// load tile sets
	private final Image[] TILE_SETS = new Image[] { new Image("/resources/roguelikeMap_transparent.png"),
			new Image("/resources/roguelikeIndoor_transparent.png"),
			new Image("/resources/roguelikeChar_transparent.png") };
	
	private Image[][][] subTiles;

	public static TilesFactory getTilesFactory() {
		if (singleton == null) {
			singleton = new TilesFactory();
		}
		return singleton;
	}

	private TilesFactory() {
		
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

	public Image getMapTiles() {
		return TILE_SETS[MAP_TILES.ordinal()];
	}

	public Image getIndoorTiles() {
		return TILE_SETS[INDOOR_TILES.ordinal()];
	}

	public Image getCharTiles() {
		return TILE_SETS[CHAR_TILES.ordinal()];
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
			
			int cols = (int) ((TILE_SETS[tileSource.ordinal()].getWidth() + 1) / 17);
			int tile = tileX + cols * tileY;
			
			PixelReader reader = TILE_SETS[tileSource.ordinal()].getPixelReader();
			WritableImage newImage = new WritableImage(reader, (16 + 1) * (tile % cols), (16 + 1) * (tile / cols), 16, 16);
			
			subTiles[tileSource.ordinal()][tileX][tileY] = newImage;
		}
		
		gc.drawImage(subTiles[tileSource.ordinal()][tileX][tileY], x*size, y*size);
	}

	public void drawTile(GraphicsContext gc, TileSource tileSource, int x, int y, int size, int tile) {
		int cols;
		
		cols = (int) ((TILE_SETS[tileSource.ordinal()].getWidth() + 1) / 17);
		gc.drawImage(TILE_SETS[tileSource.ordinal()], (16 + 1) * (tile % cols), (16 + 1) * (tile / cols), 16, 16, x * size, y * size,
				size, size);
	}

}
