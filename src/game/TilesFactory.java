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
	public void drawTile(GraphicsContext gc, TileSource tileSource,  int x, int y, int size, int tile) {
		
		Image image;
		
		switch (tileSource) {
		case MAP_TILES:
			image = MAP_TILES;
			break;
			
		case INDOOR_TILES:
			image = INDOOR_TILES;
			break;
			
		case CHAR_TILES:
			image = CHAR_TILES;
			break;

		default:
			image = MAP_TILES;
			break;
		}
		
		
		
		int cols = (int) ((image.getWidth() + 1) / 17);
//		int rows = (int) ((image.getHeight() + 1) / 17);
		
		gc.drawImage(image, (16 + 1) * (tile % cols), (16 + 1) * (tile / cols), 16, 16, x * size, y * size, size, size);		
	}

}
