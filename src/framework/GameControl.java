package framework;

import engine.ImageSource;
import engine.TileFactory;
import engine.TileSource;
import entities.Entity;
import entities.EntityFactory;
import gen.Generator;
import gen.environment.Ground;
import gen.environment.Map;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class GameControl extends State {
	// map settings
	private final int size = 16;

	// singleton
	private static GameControl singleton;

	// class components
	private Map map;
	private EntityFactory fac;
	private TileFactory tileFac;

	// variables
	private int cameraX, cameraY, cameraSizeX, cameraSizeY;

	/**
	 * static method to get the singleton class object
	 * 
	 * @return
	 */
	public static GameControl getControl() {
		if (singleton == null) {
			singleton = new GameControl();
		}
		return singleton;
	}

	private GameControl() {
		// call the very important state constructor
		super();

		// generate fresh map
		map = new Generator(350, 225).newLevel();

		// add layer
		addLayer(new Canvas(map.getN() * size, map.getM() * size)); // big image
		addLayer(new Canvas(Window.SIZE_X, Window.SIZE_Y));

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
	@Override
	public void tick(int ticks) {
		fac.getPlayer().tick(ticks);

		for (Entity mob : fac.getMobs()) {
			mob.tick(ticks);
		}
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
		// shift pre
		layers.get(1).relocate(-16 * cameraX, -16 * cameraY);

		renderEntities();

	}

	/**
	 * render the map
	 */
	@SuppressWarnings("unused")
	private void renderMap() {
		// initialize render screen
		final int ID = 1;
		final GraphicsContext gc = gcs.get(ID);
		gc.clearRect(0, 0, layers.get(ID).getWidth(), layers.get(ID).getHeight());

		for (int x = 0; x < cameraSizeX; x++) {
			for (int y = 0; y < cameraSizeY; y++) {
				Ground ground = map.getGround(x + cameraX, y + cameraY);

				if (ground != Ground.WALL) {
					// render tile
					drawTile(gc, x, y, ground, map.getTileNumber(x + cameraX, y + cameraY));

				} else {
					// fallback into colored squares
					gc.setFill(Window.groundColor[ground.ordinal()]);
					gc.fillRect(x * size, y * size, size, size);

				}
			}
		}
	}

	/**
	 * prerenders the map to make it unnecessary to rerender the same map every
	 * tick (huge improvement)
	 */
	private void prerenderMap() {
		// initialize render screen
		final int ID = 1;
		final GraphicsContext gc = gcs.get(ID);
		gc.clearRect(0, 0, layers.get(ID).getWidth(), layers.get(ID).getHeight());

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
		final int ID = 2;
		final GraphicsContext gc = gcs.get(ID);
		gc.clearRect(0, 0, layers.get(ID).getWidth(), layers.get(ID).getHeight());

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
