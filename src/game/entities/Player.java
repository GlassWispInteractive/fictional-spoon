package game.entities;

import engine.ImageSource;
import engine.TileFactory;
import engine.TileSource;
import framework.EventControl;
import framework.GameControl;
import game.combat.CombatEntity;
import generation.Map;
import javafx.scene.canvas.GraphicsContext;

public class Player extends CombatEntity {
	private static Player newestInstance;

	private String name = "Spieler";

	// for speed
	private int blocked = 0;

	TileFactory tileFac;

	public Player(int x, int y) {
		super(x, y, 15, 1);

		// values settings
		this.delayTicks = 4;

		// set references - not in entity list
		tileFac = TileFactory.getTilesFactory();
		newestInstance = this;
		Entity.remove(this);
	}

	public static Player getNewest() {
		return newestInstance;
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {

		ImageSource imgSource = new ImageSource(TileSource.CHAR_TILES, 0, 8);
		tileFac.drawTile(gc, imgSource, (x - offsetX), (y - offsetY), size);
	}

	@Override
	public void tick(int ticks) {
		EventControl e = EventControl.getEvents();
		int newX = x, newY = y;
		boolean moved = false;

		if (blocked >= 0) {
			blocked -= ticks;
		}

		if (e.isLeft()) {
			newX--;
			moved = true;
		} else if (e.isRight()) {
			newX++;
			moved = true;
		} else if (e.isUp()) {
			newY--;
			moved = true;
		} else if (e.isDown()) {
			newY++;
			moved = true;
		}

		Map map = GameControl.getControl().getMap();

		if (moved && map.isWalkable(newX, newY) && blocked <= 0) {

			blocked = delayTicks;

			x = newX;
			y = newY;

			GameControl.getControl().updateCamera(x, y);
		}

	}

	/**
	 * heals the player
	 */
	public void heal() {
		life = maxLife;
	}

	@Override
	public String toString() {
		return "" + name + " [" + life + " / " + maxLife + "]";
	}
}
