package game;

import entities.Entity;
import entities.EntityFactory;
import gen.Generator;
import gen.environment.Ground;
import gen.environment.Map;
import javafx.scene.canvas.GraphicsContext;
import static game.State.*;

import engine.Tileset;

public class World {

	/** SINGELTON */
	private static World singleton;

	// class components
	private Generator gen;
	private Map map;
	private EntityFactory fac;
	private Game game;

	// variables
	private int size = 5;
	private int offsetX, offsetY, viewSizeX, viewSizeY;

	// b_0 b_1 b_2 b_3 -> left right top bottom
	// binary counting with 1 means that area is walkable
	private final int[][] tileNumber = new int[][] { new int[] { 9, 9 }, // fail
			new int[] { 0, 0 }, // 0 0 0 1
			new int[] { 2, 0 }, // 0 0 1 0
			new int[] { 1, 0 }, // 0 0 1 1
			new int[] { 0, 2 }, // 0 1 0 0
			new int[] { 3, 1 }, // 0 1 0 1
			new int[] { 3, 2 }, // 0 1 1 0
			new int[] { 6, 0 }, // 0 1 1 1
			new int[] { 0, 1 }, // 1 0 0 0
			new int[] { 0, 3 }, // 1 0 0 1
			new int[] { 4, 0 }, // 1 0 1 0
			new int[] { 6, 1 }, // 1 0 1 1
			new int[] { 2, 1 }, // 1 1 0 0
			new int[] { 5, 0 }, // 1 1 0 1
			new int[] { 5, 1 }, // 1 1 1 0
			new int[] { 1, 1 }, // 1 1 1 1
	};

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

		updateView();
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
		fac.smartDeletNow();

		if (Events.getEvents().isESC()) {
			game.setState(MENU);
		}
		if (Events.getEvents().isM()) {
			if (game.getState() == MAP) {
				Entity player = EntityFactory.getFactory().getPlayer();

				game.setState(VIEW);
				updateView();
				initView(player.getX(), player.getY());
			} else {
				game.setState(MAP);
				updateView();
			}
			Events.getEvents().clear();
		}
	}

	public void render(GraphicsContext gc) {
		// set color and render ground tile
		// if (Game.getGame().getState() != MAP)
		for (int x = 0; x < viewSizeX; x++) {
			for (int y = 0; y < viewSizeY; y++) {
				if (map.getGround(x + offsetX, y + offsetY) != Ground.FLOOR) {
					gc.setFill(Game.getColor(map.getGround(x + offsetX, y + offsetY)));
					gc.fillRect(x * size, y * size, size, size);
				} else {
					int tile = map.getTileNumber(x + offsetX, y + offsetY);
//					System.out.println(tile);
					gc.drawImage(Tileset.getTileset().tileset, (16 + 1) * (27 + tileNumber[tile][0]), (16 +1) * (12 + tileNumber[tile][1]), 16, 16, x * size, y * size, 32, 32);
				}
			}
		}

		// for (Entity mob : fac.getMobs()) {
		// mob.render(gc, size, offsetX, offsetY);
		// }
		// fac.getPlayer().render(gc, size, offsetX, offsetY);
	}

	public void updateView() {
		// set size parameters
		if (Game.getGame().getState() == MAP) {
			size = 4;
		} else {
			size = 32;
		}

		// set view size and be sure to be smaller than the map
		viewSizeX = Math.min(1400 / size, map.getN());
		viewSizeY = Math.min(900 / size, map.getM());

		checkOffset();
	}

	public void initView(int centerX, int centerY) {
		this.offsetX = centerX - viewSizeX / 2;
		this.offsetY = centerY - viewSizeY / 2;

		checkOffset();
	}

	public void setView(int centerX, int centerY) {
		int viewPaddingX = viewSizeX / 5; // 20%
		int viewPaddingY = viewSizeY / 5;

		if (centerX - viewPaddingX < offsetX) {
			offsetX = centerX - viewPaddingX;
		}
		if (centerX + viewPaddingX - viewSizeX > offsetX) {
			offsetX = centerX + viewPaddingX - viewSizeX;
		}
		if (centerY - viewPaddingY < offsetY) {
			offsetY = centerY - viewPaddingY;
		}
		if (centerY + viewPaddingY - viewSizeY > offsetY) {
			offsetY = centerY + viewPaddingY - viewSizeY;
		}

		checkOffset();
	}

	private void checkOffset() {
		if (offsetX < 0) {
			offsetX = 0;
		}
		if (offsetY < 0) {
			offsetY = 0;
		}

		if (offsetX >= map.getN() - viewSizeX) {
			offsetX = map.getN() - viewSizeX;
		}
		if (offsetY >= map.getM() - viewSizeY) {
			offsetY = map.getM() - viewSizeY;
		}
	}
}
