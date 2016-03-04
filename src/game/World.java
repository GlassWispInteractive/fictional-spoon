package game;

import dungeon.Generator;
import dungeon.Map;
import entities.Entity;
import entities.EntityFactory;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class World {

	/** SINGELTON */
	private static World singleton;

	// class components
	private Generator gen;
	private Map map;
	private EntityFactory fac;

	// variables
	private int size = 5;

	int offsetX = 10, offsetY = 10, viewSizeX = 140, viewSizeY = 90;
	// int offsetX = 10, offsetY = 30, viewSizeX = 50, viewSizeY = 30;

	private Paint[] color = { Paint.valueOf("#454545"), Paint.valueOf("#A1D490"), Paint.valueOf("#D4B790"),
			Paint.valueOf("#B39B7B"), Paint.valueOf("#801B1B"), Paint.valueOf("#000000") };

	public static World getWorld() {
		if (singleton == null) {

			singleton = new World();
		}
		return singleton;
	}

	private World() {
		gen = new Generator(280, 180);
		map = gen.newLevel();
		fac = EntityFactory.getFactory();
		
//		player = fac.makePlayer(15, 15);
		fac.makePlayer(15, 15);
	}

	// we should delete this function - change the map would need effects in any
	// other state as well!
	public void updateMap(Map newMap) {
		this.map = newMap;
	}

	public Map getMap() {
		return this.map;
	}

	public void setSize(int size) {
		this.size = size;

	}

	public void changeCurrentView(int offsetChangeX, int offsetChangeY) {
		this.offsetX += offsetChangeX;
		this.offsetY += offsetChangeY;

		if (this.offsetX < 0) {
			this.offsetX = 0;
		}
		if (this.offsetY < 0) {
			this.offsetY = 0;
		}
		if (this.offsetX >= map.getN() - viewSizeX) {
			this.offsetX = map.getN() - viewSizeX;
		}
		if (this.offsetY >= map.getM() - viewSizeY) {
			this.offsetY = map.getM() - viewSizeY;
		}
	}
	
	
	public void tick(double el) {
		for (Entity mob : fac.getMobs()) {
			mob.tick(el);
		}
	}

	public void render(GraphicsContext gc) {
		// set color and render ground tile
		for (int x = 0; x < map.getN(); x++) {
			for (int y = 0; y < map.getM(); y++) {
				gc.setFill(color[map.getGround(x, y).ordinal()]);
				gc.fillRect(x * size, y * size, size, size);
			}
		}
		
		for (Entity mob : fac.getMobs()) {
			mob.render(gc, size);
		}
	}
	

	public void renderPlayerView(GraphicsContext gc) {
		//
		// for (int i = 0; i < viewSizeX; i++) {
		// int x = i * fieldSizePlayer;
		// for (int j = 0; j < viewSizeY; j++) {
		// int y = j * fieldSizePlayer;
		// if (i >= map.getN() || j >= map.getM()) {
		// System.out.println("ERROR, out of map (in Level.drawPlayerView)");
		// return;
		// }
		//
		// gc.setFill(color[map.getGround(i + offsetX, j + offsetY).ordinal()]);
		// gc.fillRect(x, y, fieldSizePlayer, fieldSizePlayer);
		// }
		// }
	}

}
