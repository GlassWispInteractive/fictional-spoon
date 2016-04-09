package engine;

import javafx.scene.image.Image;

public enum TileSource {
    MAP_TILES(new Image("/resources/graphics/roguelikeMap_transparent.png"), 16, 1), INDOOR_TILES(
            new Image("/resources/graphics/roguelikeIndoor_transparent.png"), 16,
            1), CHAR_TILES(new Image("/resources/graphics/roguelikeChar_transparent.png"), 16,
                    1), MONSTER_TILES(new Image("/resources/graphics/roguelikecreatures_noPadding.png"), 16, 0);
    // MONSTER_TILES(new Image("/resources/graphics/roguelikecreatures.png"),
    // 16, 1);
    
    Image image;
    int tileWidth, tileHeight, margin;
    
    private TileSource(Image image, int tileSquare, int margin) {
        this(image, tileSquare, tileSquare, margin);
    }
    
    private TileSource(Image image, int tileWidth, int tileHeight, int margin) {
        this.image = image;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.margin = margin;
    }
    
    public Image getImage() {
        return image;
    }
    
    public int getTileWidth() {
        return tileWidth;
    }
    
    public int getTileHeight() {
        return tileHeight;
    }
    
    public int getMargin() {
        return margin;
    }
}
