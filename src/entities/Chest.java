package entities;

import game.ImageSource;
import game.TileFactory;
import game.TileSource;
import javafx.scene.canvas.GraphicsContext;

public class Chest extends Entity {

	private boolean chestOpen = false;
	TileFactory tileFac;

	public Chest(int x, int y) {
		super(x, y);
		tileFac = TileFactory.getTilesFactory();
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {

		if (chestOpen) {
			ImageSource imgsource = new ImageSource(TileSource.MAP_TILES, 15, 7);
			tileFac.drawTile(gc, imgsource, (x - offsetX), (y - offsetY), size);
		} else {
			ImageSource imgsource = new ImageSource(TileSource.MAP_TILES, 14, 6);
			tileFac.drawTile(gc, imgsource, (x - offsetX), (y - offsetY), size);
		}
	}

	@Override
	public void tick(double elapsedTime) {
		// check intersection
		EntityFactory fac = EntityFactory.getFactory();
		if (x == fac.getPlayer().getX() && y == fac.getPlayer().getY()) {
			chestOpen = true;
		}

	}

}
