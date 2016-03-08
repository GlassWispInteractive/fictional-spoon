package entities;

import game.Events;
import game.TileSource;
import game.TileFactory;
import game.World;
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

		int tileX = 0;
		int tileY = 8;
		tileFac.drawTile(gc, TileSource.CHAR_TILES, (x - offsetX), (y - offsetY), size, tileX, tileY);
	}

	@Override
	public void tick(double elapsedTime) {
		Events e = Events.getEvents();
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

		Map map = World.getWorld().getMap();

		if (moved && map.isWalkable(newX, newY) && blocked <= 0) {

			blocked = delayTicks - 1;

			x = newX;
			y = newY;

			World.getWorld().setCamera(x, y);
		}

	}
}
