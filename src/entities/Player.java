package entities;

import dungeon.Ground;
import game.Events;
import game.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Player extends Entity {
	
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

		if (e.isLeft()) {
			newX--;
		}
		if (e.isRight()) {
			newX++;
		}
		if (e.isUp()) {
			newY--;
		}
		if (e.isDown()) {
			newY++;
		}

		Ground newGround = World.getWorld().getMap().getGround(newX, newY);

		if (newGround == Ground.ROOM || newGround == Ground.FLOOR) {
			
			World.getWorld().changeCurrentView(newX - x, newY - y);
			
			x = newX;
			y = newY;
		}
	}

}
