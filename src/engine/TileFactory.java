package engine;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class TileFactory {
    
    private static TileFactory singleton;
    
    private Image[][][] tiles;
    
    public static TileFactory getTilesFactory() {
        if (singleton == null) {
            singleton = new TileFactory();
        }
        return singleton;
    }
    
    private TileFactory() {
        // init the image cache array with all nulls
        int sourceCount = TileSource.values().length;
        
        tiles = new Image[sourceCount][][];
        for (int i = 0; i < sourceCount; i++) {
            TileSource source = TileSource.values()[i];
            int cols = (int) ((source.getImage().getWidth() + 1) / (source.getTileWidth() + source.getMargin()));
            
            tiles[i] = new Image[cols][];
            for (int j = 0; j < cols; j++) {
                int rows = (int) ((source.getImage().getHeight() + 1) / (source.getTileWidth() + source.getMargin()));
                tiles[i][j] = new Image[rows];
            }
        }
    }
    
    public Image getImage(TileSource source, int tileX, int tileY) {
        // try to get image from cache
        if (tiles[source.ordinal()][tileX][tileY] != null) {
            return tiles[source.ordinal()][tileX][tileY];
        }
        
        // generate new image from sheet
        PixelReader reader = source.getImage().getPixelReader();
        WritableImage newImage = new WritableImage(reader, (source.getTileWidth() + source.getMargin()) * tileX,
                (source.getTileHeight() + source.getMargin()) * tileY, source.getTileWidth(), source.getTileHeight());
                
        // put new image in cache
        tiles[source.ordinal()][tileX][tileY] = newImage;
        
        return newImage;
        
    }
}
