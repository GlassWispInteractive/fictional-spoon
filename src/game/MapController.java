package game;

import entities.Entity;
import entities.EntityFactory;
import gen.Generator;
import gen.environment.Ground;
import gen.environment.Map;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class MapController extends GameScene {

	/** SINGELTON */
	private static MapController singleton;

	// class components
	private Map map;

	private EntityFactory fac;

	private TileFactory tileFac;

	// variables
	private int size;
	private int cameraX, cameraY, cameraSizeX, cameraSizeY;

	/**
	 * static method to get the singleton class object
	 * 
	 * @return
	 */
	public static MapController getWorld() {
		if (singleton == null) {

			singleton = new MapController();
		}
		return singleton;
	}

	private MapController() {
		super();

		// add second layer
		addLayer(new Canvas(Window.SIZE_X, Window.SIZE_Y));

		// generate fresh map
		map = new Generator(350, 225).newLevel();

		// load factories
		tileFac = TileFactory.getTilesFactory();
		fac = EntityFactory.getFactory();

		// set view size and be sure to be smaller than the map
		size = 16;
		cameraSizeX = Math.min(Window.SIZE_X / size, map.getN());
		cameraSizeY = Math.min(Window.SIZE_Y / size, map.getM());

		// System.out.println(cameraSizeX);
		// System.out.println(Window.WINDOW_X / size);
		// initView();
	}

	public Map getMap() {
		return this.map;
	}

	public void setSize(int size) {
		this.size = size;

	}

	public void tick(double elapsedTime) {
		fac.getPlayer().tick(elapsedTime);

		for (Entity mob : fac.getMobs()) {
			mob.tick(elapsedTime);
		}
		fac.smartDelete();

		// if (Events.getEvents().isM()) {
		// if (game.getState() == MAP) {
		// Entity player = EntityFactory.getFactory().getPlayer();
		//
		// game.setState(VIEW);
		// updateView();
		// initCamera(player.getX(), player.getY());
		// } else {
		// game.setState(MAP);
		// updateView();
		// }
		// Events.getEvents().clear();
		// }
	}

	public void render() {
		// start from clean screen
		GraphicsContext gc = gcs.get(0);
		gc.clearRect(0, 0, layers.get(0).getWidth(), layers.get(0).getHeight());
		
		 renderMap();
		 renderEntities();
	}

	public void renderMap() {
		// start from clean screen
		GraphicsContext gc = gcs.get(0);
		gc.clearRect(0, 0, layers.get(0).getWidth(), layers.get(0).getHeight());

		for (int x = 0; x < cameraSizeX; x++) {
			for (int y = 0; y < cameraSizeY; y++) {
				if (map.getGround(x + cameraX, y + cameraY) != Ground.WALL) {

					drawMapTile(gc, x, y, map.getGround(x + cameraX, y + cameraY),
							map.getTileNumber(x + cameraX, y + cameraY));

				} else {
					gc.setFill(Game.getColor(map.getGround(x + cameraX, y + cameraY)));
					gc.fillRect(x * size, y * size, size, size);

				}
			}
		}
	}

	public void renderEntities() {
		// start from clean screen
		GraphicsContext gc = gcs.get(1);
		gc.clearRect(0, 0, layers.get(1).getWidth(), layers.get(1).getHeight());

		for (Entity mob : fac.getMobs()) {
			mob.render(gc, size, cameraX, cameraY);
		}
		fac.getPlayer().render(gc, size, cameraX, cameraY);
	}

	private void drawMapTile(GraphicsContext gc, int x, int y, Ground ground, int tile) {

		// offset in tile set
		if (ground == Ground.FLOOR)
			tile += 20 + 57 * 12;
		if (ground == Ground.ROOM)
			tile += 20 + 57 * 12;// +-7

		ImageSource imgsource = new ImageSource(TileSource.MAP_TILES, tile % 57, tile / 57);

		tileFac.drawTile(gc, imgsource, x, y, size);
	}

	public void initCamera(int centerX, int centerY) {
		this.cameraX = centerX - cameraSizeX / 2;
		this.cameraY = centerY - cameraSizeY / 2;

		fixCamera();
	}

	public void setCamera(int centerX, int centerY) {
		int viewPaddingX = cameraSizeX / 5; // 20%
		int viewPaddingY = cameraSizeY / 5;

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

		fixCamera();
	}

	private void fixCamera() {
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
}
