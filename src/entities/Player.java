package entities;

import game.Events;
import game.World;
import gen.environment.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Player extends Entity {

	private int hp = 100;
	private int mana = 100;

	// for speed
	private int blocked = 0;

	public Player(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {
		gc.setFill(Color.RED);
		gc.fillOval((x - offsetX) * size, (y - offsetY) * size, size, size);

	}

	// public void setImage(String filename) {
	// Image i = new Image(filename);
	// setImage(i);
	// }

	// public void setImage(Image i) {
	// image = i;
	// width = i.getWidth();
	// height = i.getHeight();
	// }

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

			World.getWorld().changeCurrentView(x, y);
		}

	}
}
