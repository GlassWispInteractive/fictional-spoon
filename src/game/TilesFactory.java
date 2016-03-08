package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class TilesFactory {

	private static TilesFactory singleton;
	
	// load tile sets
	private final Image MAP_TILES = new Image("/resources/roguelikeMap_transparent.png");
	private final Image INDOOR_TILES = new Image("/resources/roguelikeIndoor_transparent.png");
	private final Image CHAR_TILES = new Image("/resources/roguelikeChar_transparent.png");
	
	
	public static TilesFactory getTilesFactory(){		
		if(singleton == null){
			singleton = new TilesFactory();
		}
		return singleton;		
	}
	
	private TilesFactory (){
		
	}
	
	public Image getMapTiles(){
		return MAP_TILES;
	}
	public Image getIndoorTiles(){
		return INDOOR_TILES;
	}
	public Image getCharTiles(){
		return CHAR_TILES;
	}
	
	
	//draw tiles  
	/**
	 * 
	 * *TO LAGGY !?*
	 * 
	 */
	public void drawTile(GraphicsContext gc, TileSource tileSource,  int x, int y, int size, int tileX, int tileY) {
		int cols;
		switch (tileSource) {
		case MAP_TILES:
			cols = (int) ((MAP_TILES.getWidth() + 1) / 17);
			break;
			
		case INDOOR_TILES:
			cols = (int) ((INDOOR_TILES.getWidth() + 1) / 17);
			break;
			
		case CHAR_TILES:
			cols = (int) ((CHAR_TILES.getWidth() + 1) / 17);
			break;

		default:
			cols = -1;
			System.err.println("Wrong tileSource");
			break;
		}	
		int tile = tileX + cols * tileY;
		
		drawTile(gc, tileSource, x, y, size, tile);
	}
	public void drawTile(GraphicsContext gc, TileSource tileSource,  int x, int y, int size, int tile) {
		int cols;
		
		switch (tileSource) {
		case MAP_TILES:
			cols = (int) ((MAP_TILES.getWidth() + 1) / 17);
			gc.drawImage(MAP_TILES, (16 + 1) * (tile % cols), (16 + 1) * (tile / cols), 16, 16, x * size, y * size, size, size);	
			break;
			
		case INDOOR_TILES:
			cols = (int) ((INDOOR_TILES.getWidth() + 1) / 17);
			gc.drawImage(INDOOR_TILES, (16 + 1) * (tile % cols), (16 + 1) * (tile / cols), 16, 16, x * size, y * size, size, size);	
			break;
			
		case CHAR_TILES:
			cols = (int) ((CHAR_TILES.getWidth() + 1) / 17);
			gc.drawImage(CHAR_TILES, (16 + 1) * (tile % cols), (16 + 1) * (tile / cols), 16, 16, x * size, y * size, size, size);	
			break;

		default:
			break;
		}	
	}

}
