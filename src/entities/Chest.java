package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Chest extends Entity {
	
	private boolean chestOpen = false;

	public Chest(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {
		gc.setFill(Color.BROWN);
		gc.fillRect((x - offsetX) * size + 1, (y - offsetY) * size + 1, size - 2, size - 2);
		
		if(!chestOpen){
			gc.setFill(Color.BLACK);
			gc.fillRect((x - offsetX) * size + 1, (y - offsetY) * size + 1, size - 2, size/2);
		}
	}

	@Override
	public void tick(double elapsedTime) {
		// check intersection
		EntityFactory fac = EntityFactory.getFactory();
		if(x == fac.getPlayer().getX() && y == fac.getPlayer().getY()){
			chestOpen = true;
		}

	}

}
