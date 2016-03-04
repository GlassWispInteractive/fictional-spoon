package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Monster extends Entity {

	public Monster(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {
		gc.setFill(Color.DARKBLUE);
		gc.fillRect((x - offsetX) * size + 1, (y - offsetY) * size + 1, size - 2, size - 2);
	}

	@Override
	public void tick(double elapsedTime) {
		// TODO Auto-generated method stub
		
	}

}
