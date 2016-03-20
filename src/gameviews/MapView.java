package gameviews;

import engine.ImageSource;
import engine.TileFactory;
import engine.TileSource;
import entities.Entity;
import entities.EntityFactory;
import framework.Window;
import generation.Ground;
import generation.LevelBuilder;
import generation.Map;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class MapView extends GameView {
	// map settings
	private final int size = 16;

	// class components
	private Canvas bg;
	private Map map;
	private EntityFactory fac;
	private TileFactory tileFac;

	// variables
	private int cameraX, cameraY, cameraSizeX, cameraSizeY;

	public MapView(Canvas mapLayer, Canvas entitiesLayer) {
		// call the very important state constructor
		super(entitiesLayer);
		bg = new Canvas(350 * size, 225 * size);
		
		// generate fresh map
		map = LevelBuilder.newLevel(350, 225);

		// load factories
		tileFac = TileFactory.getTilesFactory();
		fac = EntityFactory.getFactory();

		// set view size and be sure to be smaller than the map
		cameraSizeX = Math.min(Window.SIZE_X / size, map.getN());
		cameraSizeY = Math.min(Window.SIZE_Y / size, map.getM());

		// render the map prior every other rendering and keep it cached
		prerenderMap();

		// set view
		Entity player = EntityFactory.getFactory().getPlayer();
		initCamera(player.getX(), player.getY());
	}

	/**
	 * @return the map
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * method is called every tick
	 * 
	 * @param ticks
	 */
	public void tick(int ticks) {
		fac.getPlayer().tick(ticks);

		for (Entity mob : fac.getMobs()) {
			mob.tick(ticks);
		}
		fac.smartAdd();
		fac.smartDelete();
	}

	/**
	 * initiaze the camera view
	 * 
	 * @param centerX
	 * @param centerY
	 */
	public void initCamera(int centerX, int centerY) {
		this.cameraX = centerX - cameraSizeX / 2;
		this.cameraY = centerY - cameraSizeY / 2;

		alignCamera();
	}

	/**
	 * updates the camera view the view is only chaning when the player moves
	 * close to the border
	 * 
	 * @param centerX
	 * @param centerY
	 */
	public void updateCamera(int centerX, int centerY) {
		// ~15% of the screen is the
		final int viewPaddingX = cameraSizeX / 7;
		final int viewPaddingY = cameraSizeY / 7;

		if (centerX - viewPaddingX < cameraX) {
			cameraX = centerX - viewPaddingX;
		}
		if (centerX + viewPaddingX - cameraSizeX > cameraX) {
			cameraX = centerX + viewPaddingX - cameraSizeX;
		}
		if (centerY - viewPaddingY < cameraY) {
			cameraY = centerY - viewPaddingY;
		}
		if (centerY + viewPaddingY - cameraSizeY > cameraY) {
			cameraY = centerY + viewPaddingY - cameraSizeY;
		}

		alignCamera();
	}

	/**
	 * aligns the camera according to the displayable screen
	 */
	private void alignCamera() {
		if (cameraX < 0) {
			cameraX = 0;
		}
		if (cameraY < 0) {
			cameraY = 0;
		}

		if (cameraX >= map.getN() - cameraSizeX) {
			cameraX = map.getN() - cameraSizeX;
		}
		if (cameraY >= map.getM() - cameraSizeY) {
			cameraY = map.getM() - cameraSizeY;
		}
	}

	@Override
	public void render() {
		// shift prerendered map
//		bg.relocate(-16 * cameraX, -16 * cameraY);

		renderEntities();

	}

	/**
	 * prerenders the map to make it unnecessary to rerender the same map every
	 * tick (huge improvement)
	 */
	private void prerenderMap() {
		// initialize render screen
		gc.clearRect(0, 0, Window.SIZE_X, Window.SIZE_Y);
//		GraphicsContext gc = .getGraphicsContext2D();

		// full rendering of the map
		for (int x = 0; x < map.getN(); x++) {
			for (int y = 0; y < map.getM(); y++) {
				if (map.getGround(x, y) != Ground.WALL) {
					drawTile(gc, x, y, map.getGround(x, y), map.getTileNumber(x, y));
				}
			}
		}
	}

	/**
	 * render the entities
	 */
	private void renderEntities() {
		// initialize render screen
//		final GraphicsContext gc = gcs.get("entities");
		gc.clearRect(0, 0, layer.getWidth(), layer.getHeight());

		for (Entity mob : fac.getMobs()) {
			mob.render(gc, size, cameraX, cameraY);
		}
		fac.getPlayer().render(gc, size, cameraX, cameraY);
	}

	/**
	 * helper function to draw tiles onto the gc object
	 * 
	 * @param gc
	 * @param x
	 * @param y
	 * @param ground
	 * @param tile
	 */
	private void drawTile(GraphicsContext gc, int x, int y, Ground ground, int tile) {

		// offset in tile set
		if (ground == Ground.FLOOR)
			tile += 20 + 57 * 12;
		if (ground == Ground.ROOM)
			tile += 20 + 57 * 12;// +-7

		ImageSource imgsource = new ImageSource(TileSource.MAP_TILES, tile % 57, tile / 57);

		tileFac.drawTile(gc, imgsource, x, y, size);
	}
}
