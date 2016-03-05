package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Shrine extends Entity {
	private int blocked = 0;

	public Shrine(int x, int y) {
		super(x, y);
		delayTicks = 1500;
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {

		if (blocked == 0) {
			gc.setFill(Color.YELLOW);
			gc.fillOval((x - offsetX) * size + size / 4 - 2, (y - offsetY) * size + size / 4 - 2, size / 2 + 4,
					size / 2 + 4);
		}

		gc.setFill(Color.BLACK);
		gc.fillOval((x - offsetX) * size + size / 4, (y - offsetY) * size + size / 4, size / 2, size / 2);

	}

	@Override
	public void tick(double elapsedTime) {
		// check intersection
		EntityFactory fac = EntityFactory.getFactory();
		
		if (x == fac.getPlayer().getX() && y == fac.getPlayer().getY() && blocked == 0) {
			blocked = delayTicks;
		}
		
		if (blocked > 0)
			blocked--;
	}

}
