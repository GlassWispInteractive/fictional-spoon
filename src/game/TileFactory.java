package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class TileFactory {

	private static TileFactory singleton;

	// load tile sets
	private final Image[] TILE_SETS = new Image[] { new Image("/resources/roguelikeMap_transparent.png"),
			new Image("/resources/roguelikeIndoor_transparent.png"),
			new Image("/resources/roguelikeChar_transparent.png"), new Image("/resources/roguelikecreatures.png") };

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

		// 1st dim. count of different ressauces
		for (int i = 0; i < sourceCount; i++) {
			// 2. dim number of cols
			int cols = (int) ((TILE_SETS[i].getWidth() + 1) / 17);
			subTiles[i] = new Image[cols][];
			for (int j = 0; j < cols; j++) {
				// 3. dim number of rows
				int rows = (int) ((TILE_SETS[i].getHeight() + 1) / 17);
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
	public void drawTile(GraphicsContext gc, ImageSource imgSource, int x, int y, int size) {

		if (subTiles[imgSource.getTileSourceOrdinal()][imgSource.getTileX()][imgSource.getTileY()] == null) {

			loadImageSource(imgSource);
		}

		gc.drawImage(subTiles[imgSource.getTileSourceOrdinal()][imgSource.getTileX()][imgSource.getTileY()], x * size,
				y * size);
	}

	public Image scale(ImageSource imgSource, int scaling) {

		Image source = subTiles[imgSource.getTileSourceOrdinal()][imgSource.getTileX()][imgSource.getTileY()];

		if (source == null) {
			loadImageSource(imgSource);
		}

		source = subTiles[imgSource.getTileSourceOrdinal()][imgSource.getTileX()][imgSource.getTileY()];

		return scale(imgSource, (int) source.getWidth() * scaling, (int) source.getHeight() * scaling);
	}

	public Image getImage(ImageSource imgSource) {

		Image source = subTiles[imgSource.getTileSourceOrdinal()][imgSource.getTileX()][imgSource.getTileY()];

		if (source == null) {
			loadImageSource(imgSource);
		}

		return subTiles[imgSource.getTileSourceOrdinal()][imgSource.getTileX()][imgSource.getTileY()];

	}

	public Image scale(ImageSource imgSource, int newWidth, int newHeight) {

		Image source = subTiles[imgSource.getTileSourceOrdinal()][imgSource.getTileX()][imgSource.getTileY()];

		if (source == null) {
			loadImageSource(imgSource);
		}

		source = subTiles[imgSource.getTileSourceOrdinal()][imgSource.getTileX()][imgSource.getTileY()];

		ImageView imageView = new ImageView(source);
		imageView.setPreserveRatio(true);
		imageView.setFitWidth(newWidth);
		imageView.setFitHeight(newHeight);
		return imageView.snapshot(null, null);
	}

	private void loadImageSource(ImageSource imgSource) {
		PixelReader reader = TILE_SETS[imgSource.getTileSourceOrdinal()].getPixelReader();
		WritableImage newImage = new WritableImage(reader, (16 + 1) * imgSource.getTileX(),
				(16 + 1) * imgSource.getTileY(), 16, 16);

		subTiles[imgSource.getTileSourceOrdinal()][imgSource.getTileX()][imgSource.getTileY()] = newImage;
	}
}
