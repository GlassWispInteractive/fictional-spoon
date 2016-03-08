package entities;

import game.Events;
import game.TileSource;
import game.TilesFactory;
import game.World;
import gen.environment.Map;
import javafx.scene.canvas.GraphicsContext;

public class Player extends Entity {

	@SuppressWarnings("unused")
	private int hp = 100;

	// for speed
	private int blocked = 0;
	TilesFactory tileFac;

	public Player(int x, int y) {
		super(x, y);
		delayTicks = 4;
		tileFac = TilesFactory.getTilesFactory();
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {
		
		int tileX = 0;
		int tileY = 8;
		tileFac.drawTile(gc, TileSource.CHAR_TILES, (x - offsetX), (y - offsetY), size, tileX, tileY);
//		gc.drawImage(tileFac.getCharTiles(), (16 + 1) * (tile % 54), (16 + 1) * (tile / 54), 16, 16, (x - offsetX) * size, (y - offsetY) * size, size, size);
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
