package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Shrine extends Entity {

	public Shrine(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {
		gc.setFill(Color.WHITE);
		gc.fillOval((x - offsetX) * size + size / 4, (y - offsetY) * size + size / 4, size / 2, size / 2);
	}

	@Override
	public void tick(double elapsedTime) {
		// TODO Auto-generated method stub

	}

}
