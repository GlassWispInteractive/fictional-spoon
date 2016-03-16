package entities;

import java.util.ArrayList;

import combat.Combo;
import engine.ImageSource;
import engine.TileFactory;
import engine.TileSource;
import javafx.scene.canvas.GraphicsContext;

public class Chest extends Entity {
	private static ArrayList<Chest> chests = new ArrayList<>();;

	private Combo item;
	TileFactory tileFac;

	public Chest(int x, int y, Combo combo) {
		super(x, y);
		this.item = combo;
		
		chests.add(this);
		
		tileFac = TileFactory.getTilesFactory();
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {

		if (item == null) {
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
		if (x == fac.getPlayer().getX() && y == fac.getPlayer().getY() && item != null) {
			item.activate();
			item = null;
		}

	}

}
