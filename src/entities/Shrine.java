package entities;

import game.ImageSource;
import game.TileFactory;
import game.TileSource;
import javafx.scene.canvas.GraphicsContext;

public class Shrine extends Entity {
	private int blocked = 0;
	TileFactory tileFac;

	public Shrine(int x, int y) {
		super(x, y);
		delayTicks = 1500;
		tileFac = TileFactory.getTilesFactory();
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {
		
		if(blocked == 0){
			ImageSource imgsource = new ImageSource(TileSource.MAP_TILES, 43, 10);
			tileFac.drawTile(gc, imgsource, (x - offsetX), (y - offsetY), size);
		}else{
			ImageSource imgsource = new ImageSource(TileSource.MAP_TILES, 41, 10);
			tileFac.drawTile(gc, imgsource, (x - offsetX), (y - offsetY), size);
		}

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
