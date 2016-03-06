package game;

import entities.Entity;
import entities.EntityFactory;
import gen.Generator;
import gen.environment.Ground;
import gen.environment.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static game.State.*;

public class World {

	/** SINGELTON */
	private static World singleton;

	// class components
	private Generator gen;
	private Map map;
	private EntityFactory fac;
	private Game game;

	// variables
	private int size;
	private int cameraX, cameraY, viewSizeX, viewSizeY;

	// load tile sets
	private final Image MAP_TILES = new Image("/resources/roguelikeMap_transparent.png");
	@SuppressWarnings("unused")
	private final Image INDOOR_TILES = new Image("/resources/roguelikeIndoor_transparent.png");
	@SuppressWarnings("unused")
	private final Image CHAR_TILES = new Image("/resources/roguelikeChar_transparent.png");

	/**
	 * static method to get the singleton class object
	 * 
	 * @return
	 */
	public static World getWorld() {
		if (singleton == null) {

			singleton = new World();
		}
		return singleton;
	}

	private World() {
		gen = new Generator(350, 225);
		map = gen.newLevel();
		fac = EntityFactory.getFactory();
		game = Game.getGame();

		// set view size and be sure to be smaller than the map
		size = 16;
		viewSizeX = Math.min(1400 / size, map.getN());
		viewSizeY = Math.min(900 / size, map.getM());

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

		if (Events.getEvents().isESC()) {
			game.setState(MENU);
		}

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

	public void render(GraphicsContext gc) {
		// set color and render ground tile
		// if (Game.getGame().getState() != MAP)
		for (int x = 0; x < viewSizeX; x++) {
			for (int y = 0; y < viewSizeY; y++) {
				if (map.getGround(x + cameraX, y + cameraY) != Ground.WALL) {

					drawMapTile(gc, x, y, map.getTileNumber(x + cameraX, y + cameraY));

				} else {
					gc.setFill(Game.getColor(map.getGround(x + cameraX, y + cameraY)));
					gc.fillRect(x * size, y * size, size, size);

				}
			}
		}

		for (Entity mob : fac.getMobs()) {
			mob.render(gc, size, cameraX, cameraY);
		}
		fac.getPlayer().render(gc, size, cameraX, cameraY);

	}

	private void drawMapTile(GraphicsContext gc, int x, int y, int tile) {
		// final double cols = (MAP_TILES.getWidth() + 1) / 17;
		// final double rows = (MAP_TILES.getHeight() + 1) / 17;

		// offset in tile set
		tile += 20 + 57 * 12;

		gc.drawImage(MAP_TILES, (16 + 1) * (tile % 57), (16 + 1) * (tile / 57), 16, 16, x * size, y * size, size, size);

	}

	public void initCamera(int centerX, int centerY) {
		this.cameraX = centerX - viewSizeX / 2;
		this.cameraY = centerY - viewSizeY / 2;

		fixCamera();
	}

	public void setCamera(int centerX, int centerY) {
		int viewPaddingX = viewSizeX / 5; // 20%
		int viewPaddingY = viewSizeY / 5;

		if (centerX - viewPaddingX < cameraX) {
			cameraX = centerX - viewPaddingX;
		}
		if (centerX + viewPaddingX - viewSizeX > cameraX) {
			cameraX = centerX + viewPaddingX - viewSizeX;
		}
		if (centerY - viewPaddingY < cameraY) {
			cameraY = centerY - viewPaddingY;
		}
		if (centerY + viewPaddingY - viewSizeY > cameraY) {
			cameraY = centerY + viewPaddingY - viewSizeY;
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

		if (cameraX >= map.getN() - viewSizeX) {
			cameraX = map.getN() - viewSizeX;
		}
		if (cameraY >= map.getM() - viewSizeY) {
			cameraY = map.getM() - viewSizeY;
		}
	}
}
