package entities;

import engine.ImageSource;
import engine.TileFactory;
import engine.TileSource;
import framework.EventControl;
import framework.GameControl;
import gen.environment.Map;
import javafx.scene.canvas.GraphicsContext;

public class Player extends Entity {

	@SuppressWarnings("unused")
	private int hp = 100;

	// for speed
	private int blocked = 0;

	@SuppressWarnings("unused")
	private int souls[];
	TileFactory tileFac;

	public Player(int x, int y) {
		super(x, y);
		delayTicks = 4;

		souls = new int[] { 1, 1, 1, 1, 1 };
		tileFac = TileFactory.getTilesFactory();
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {

		ImageSource imgSource = new ImageSource(TileSource.CHAR_TILES, 0, 8);
		tileFac.drawTile(gc, imgSource, (x - offsetX), (y - offsetY), size);
	}

	@Override
	public void tick(double elapsedTime) {
		EventControl e = EventControl.getEvents();
		int newX = x, newY = y;
		boolean moved = false;

		if (blocked >= 0) {
			blocked--;
		}

		if (e.isLeft()) {
			newX--;
			moved = true;
		}
		if (e.isRight()) {
			newX++;
			moved = true;
		}
		if (e.isUp()) {
			newY--;
			moved = true;
		}
		if (e.isDown()) {
			newY++;
			moved = true;
		}

		Map map = GameControl.getControl().getMap();

		if (moved && map.isWalkable(newX, newY) && blocked <= 0) {

			blocked = delayTicks - 1;

			x = newX;
			y = newY;

			GameControl.getControl().updateCamera(x, y);
		}

	}
}
