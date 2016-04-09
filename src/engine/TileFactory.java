package engine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class TileFactory {
    
    private static TileFactory singleton;
    
    private Image[][][] subTiles;
    
    public static TileFactory getTilesFactory() {
        if (singleton == null) {
            singleton = new TileFactory();
        }
        return singleton;
    }
    
    private TileFactory() {
        
        int sourceCount = TileSource.values().length;
        subTiles = new Image[sourceCount][][];
        
        // 1st dim. count of different ressauces
        for (int i = 0; i < sourceCount; i++) {
            
            TileSource source = TileSource.values()[i];
            
            // 2. dim number of cols
            int cols = (int) ((source.getImage().getWidth() + 1) / (source.getTileWidth() + source.getMargin()));
            subTiles[i] = new Image[cols][];
            for (int j = 0; j < cols; j++) {
                // 3. dim number of rows
                int rows = (int) ((source.getImage().getHeight() + 1) / (source.getTileWidth() + source.getMargin()));
                subTiles[i][j] = new Image[rows];
            }
        }
    }
    
    /**
     * 
     * @param gc
     * @param tileSource
     * @param x
     *            , coordinate for drawing
     * @param y
     *            , coordinate for drawing
     * @param size
     *            , scaling size
     * @param tileX
     *            , choose tile with coordinate (starting at 0, 0)
     * @param tileY
     *            , choose tile with coordinate (starting at 0, 0)
     */
    public void drawTile(GraphicsContext gc, TileSource source, int tileX, int tileY, int x, int y, int size) {
        Image img = getImage(source, tileX, tileY);
        gc.drawImage(img, x * size, y * size);
    }
    
    public Image getImage(TileSource source, int tileX, int tileY) {
        PixelReader reader = source.getImage().getPixelReader();
        WritableImage newImage = new WritableImage(reader, (source.getTileWidth() + source.getMargin()) * tileX,
                (source.getTileHeight() + source.getMargin()) * tileY, source.getTileWidth(), source.getTileHeight());
                
        return newImage;
        
    }
}
