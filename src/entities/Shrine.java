package entities;

import engine.ImageSource;
import engine.TileFactory;
import engine.TileSource;
import framework.GameControl;
import javafx.scene.canvas.GraphicsContext;

public class Shrine extends Entity {
	private int blocked = 0;
	private TileFactory tileFac;
	private Player player = (Player) EntityFactory.getFactory().getPlayer();
	

	public Shrine(int x, int y) {
		super(x, y);
		delayTicks = 1500;
		tileFac = TileFactory.getTilesFactory();
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {

		if (blocked == 0) {
			ImageSource imgsource = new ImageSource(TileSource.MAP_TILES, 43, 10);
			tileFac.drawTile(gc, imgsource, (x - offsetX), (y - offsetY), size);
		} else {
			ImageSource imgsource = new ImageSource(TileSource.MAP_TILES, 41, 10);
			tileFac.drawTile(gc, imgsource, (x - offsetX), (y - offsetY), size);
		}

	}

	@Override
	public void tick(double elapsedTime) {
		// check intersection
		if (x == player.getX() && y == player.getY() && blocked == 0) {
			GameControl.getControl().alert("Player health completely restored");
			player.heal();
			blocked = delayTicks;
		}

		if (blocked > 0) {
			blocked--;
		}
	}

}
