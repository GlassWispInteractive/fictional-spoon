package entities;

import game.TilesFactory;
import game.TileSource;
import javafx.scene.canvas.GraphicsContext;

public class Chest extends Entity {
	
	private boolean chestOpen = false;
	TilesFactory tileFac;

	public Chest(int x, int y) {
		super(x, y);
		tileFac = TilesFactory.getTilesFactory();
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {
		
		if(chestOpen){
			int tileX = 15;
			int tileY = 7;
			tileFac.drawTile(gc, TileSource.MAP_TILES, (x - offsetX), (y - offsetY), size, tileX, tileY);
		}else{
			int tileX = 14;
			int tileY = 6;
			tileFac.drawTile(gc, TileSource.MAP_TILES, (x - offsetX), (y - offsetY), size, tileX, tileY);
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
