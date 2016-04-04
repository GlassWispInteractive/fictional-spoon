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
	private static ArrayList<Chest> collection = new ArrayList<>();;

	private Combo item;
	TileFactory tileFac;

	private Chest(int x, int y, Combo combo) {
		super(x, y);
		this.item = combo;

		tileFac = TileFactory.getTilesFactory();
	}

	/**
	 * function generates a monster at a specified place
	 * 
	 * @param x
	 * @param y
	 * @param spawnIsInRoom
	 */
	public static void generate(int x, int y) {
		Chest obj = new Chest(x, y, Combo.generate(3));
		collection.add(obj);
	}

	/**
	 * function returns every existing monster
	 * 
	 * @return
	 */
	public static Chest[] getObjects() {
		return collection.toArray(new Chest[] {});
	}

	/**
	 * function resets the state
	 */
	public static void reset() {
		collection = new ArrayList<>();
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
