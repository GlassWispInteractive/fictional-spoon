package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Shrine extends Entity {
	
	private boolean shrineUsed = false;

	public Shrine(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {
		
		if(!shrineUsed){
			gc.setFill(Color.YELLOW);
			gc.fillOval((x - offsetX) * size + size / 4 -2, (y - offsetY) * size + size / 4 -2, size / 2 +4, size / 2 +4);
		}
		
		gc.setFill(Color.BLACK);
		gc.fillOval((x - offsetX) * size + size / 4, (y - offsetY) * size + size / 4, size / 2, size / 2);
		
	}

	@Override
	public void tick(double elapsedTime) {
		// check intersection
		EntityFactory fac = EntityFactory.getFactory();
		if(x == fac.getPlayer().getX() && y == fac.getPlayer().getY()){
			shrineUsed = true;
		}

	}

}
