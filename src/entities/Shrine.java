package entities;

import game.TilesFactory;
import game.TileSource;
import javafx.scene.canvas.GraphicsContext;

public class Shrine extends Entity {
	private int blocked = 0;
	TilesFactory tileFac;

	public Shrine(int x, int y) {
		super(x, y);
		delayTicks = 1500;
		tileFac = TilesFactory.getTilesFactory();
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {
		
		if(blocked == 0){
			int tileX = 43;
			int tileY = 10;
			tileFac.drawTile(gc, TileSource.MAP_TILES, (x - offsetX), (y - offsetY), size, tileX, tileY);
		}else{
			int tileX = 41;
			int tileY = 10;
			tileFac.drawTile(gc, TileSource.MAP_TILES, (x - offsetX), (y - offsetY), size, tileX, tileY);
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
