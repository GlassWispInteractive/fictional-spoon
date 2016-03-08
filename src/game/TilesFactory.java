package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static game.TileSource.*;

public class TilesFactory {

	private static TilesFactory singleton;

	// load tile sets
	private final Image[] TILE_SETS = new Image[] { new Image("/resources/roguelikeMap_transparent.png"),
			new Image("/resources/roguelikeIndoor_transparent.png"),
			new Image("/resources/roguelikeChar_transparent.png") };

	public static TilesFactory getTilesFactory() {
		if (singleton == null) {
			singleton = new TilesFactory();
		}
		return singleton;
	}

	private TilesFactory() {

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
		int cols = (int) ((TILE_SETS[tileSource.ordinal()].getWidth() + 1) / 17);
		
		int tile = tileX + cols * tileY;

		drawTile(gc, tileSource, x, y, size, tile);
	}

	public void drawTile(GraphicsContext gc, TileSource tileSource, int x, int y, int size, int tile) {
		int cols;
		
		cols = (int) ((TILE_SETS[tileSource.ordinal()].getWidth() + 1) / 17);
		gc.drawImage(TILE_SETS[tileSource.ordinal()], (16 + 1) * (tile % cols), (16 + 1) * (tile / cols), 16, 16, x * size, y * size,
				size, size);
	}

}
