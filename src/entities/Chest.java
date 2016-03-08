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
			int tile = 15 + 54 * 7;
//			tileFac.drawTile(gc, TileSource.MAP_TILES, (x - offsetX), (y - offsetY), size, tile);
			gc.drawImage(tileFac.getMapTiles(), (16 + 1) * (tile % 54), (16 + 1) * (tile / 54), 16, 16, (x - offsetX) * size, (y - offsetY) * size, size, size);
		}else{
			int tile = 14 + 54 * 7;
//			tileFac.drawTile(gc, TileSource.MAP_TILES, (x - offsetX), (y - offsetY), size, tile);
			gc.drawImage(tileFac.getMapTiles(), (16 + 1) * (tile % 54), (16 + 1) * (tile / 54), 16, 16, (x - offsetX) * size, (y - offsetY) * size, size, size);
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
