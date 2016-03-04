package game;

import Moneybag.Moneybag;
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
	
	public void changeState(State state){
		switch (state) {
		case MAP:
			offsetX = 0;
			offsetY = 0;
			viewSizeX = map.getN();
			viewSizeY = map.getM();
			size = 5;
			break;

		case VIEW:
			offsetX = 10;
			offsetY = 10;
			viewSizeX = 70; 			//140
			viewSizeY = 45;				//90
			size = 20;					//10
			break;
		
		default:
			throw new IllegalArgumentException("Unknown game state: " + state);
		}		
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
		for (int x = 0; x < viewSizeX; x++) {
			for (int y = 0; y < viewSizeY; y++) {
				gc.setFill(color[map.getGround(x + offsetX, y + offsetY).ordinal()]);
				gc.fillRect(x * size, y * size, size, size);
			}
		}
		
		for (Entity mob : fac.getMobs()) {
			mob.render(gc, size, offsetX, offsetY);
		}
	}
}
