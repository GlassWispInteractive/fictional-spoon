package entities;

import java.util.ArrayList;

import combat.Combo;
import combat.Goal;
import engine.ImageSource;
import engine.TileFactory;
import engine.TileSource;
import framework.GameControl;
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
	public void tick(int ticks) {
		// check intersection
		if (intersectsWithPlayer() && item != null) {
			// alert
			GameControl.getControl().alert("New combo: " + item.toString());

			// goal update
			GameControl.getControl().updateGoal(Goal.CHEST);

			// game logic
			item.activate();
			item = null;
		}

	}

}
