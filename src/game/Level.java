package game;

import java.awt.Graphics2D;

import dungeon.Generator;
import dungeon.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Level {

	/** SINGELTON */
	private static Level instance;

	private Map map;

	final int fieldSize = 5, padding = 0, rectSize = fieldSize - padding;
	int offsetX = 10, offsetY = 30, viewSizeX = 50, viewSizeY = 30;

	private Paint[] color = { Paint.valueOf("#454545"), Paint.valueOf("#A1D490"), Paint.valueOf("#D4B790"),
			Paint.valueOf("#B39B7B"), Paint.valueOf("#801B1B"), Paint.valueOf("#000000") };

	public static Level getLevel() {
		if (instance == null) {
			Generator gen = new Generator(277, 173);
			Map map = gen.newLevel();
			instance = new Level(map);
		}
		return instance;
	}

	private Level(Map initMap) {
		this.map = initMap;
	}

	// we should delete this function - change the map would need effects in any
	// other state as well!
	public void updateMap(Map newMap) {
		this.map = newMap;
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

	public void renderMap(GraphicsContext gc) {
		for (int i = 0; i < map.getN(); i++) {
			int x = i * fieldSize;
			for (int j = 0; j < map.getM(); j++) {
				int y = j * fieldSize;

				gc.setFill(color[map.getGround(i, j).ordinal()]);
				gc.fillRect(x, y, rectSize, rectSize);
			}
		}
	}

	public void drawPlayerView(Graphics2D g2) {

		for (int i = 0; i < viewSizeX; i++) {
			int x = i * fieldSize;
			for (int j = 0; j < viewSizeY; j++) {
				int y = j * fieldSize;
				if (i >= map.getN() || j >= map.getM()) {
					System.out.println("ERROR, out of map   (in Level.drawPlayerView)");
					return;
				}
				// g2.setColor(color[map.getGround(i + offsetX, j +
				// offsetY).ordinal()]);
				g2.fillRect(x, y, rectSize, rectSize);
			}
		}
	}

}
