package entities;

import engine.ImageSource;
import engine.TileFactory;
import engine.TileSource;
import framework.GameControl;

import java.awt.Point;
import java.util.Random;

import combat.Goal;
import javafx.scene.canvas.GraphicsContext;

public class Portal extends Entity {
	private Random rnd = new Random();
	private TileFactory tileFac = TileFactory.getTilesFactory();
	// private ImageSource imgSource = new ImageSource(TileSource.MAP_TILES, 23,
	// 7); //alternative
	private ImageSource imgSource = new ImageSource(TileSource.MAP_TILES, 40, 9);
	private ImageSource imgSourceDestroyed = new ImageSource(TileSource.MAP_TILES, 44, 9);
	// private ImageSource imgSource = new ImageSource(TileSource.MAP_TILES, 36,
	// 1); //alternative
	private int blocked = 0;
	private int maxMonsterSpawn = 3;
	private boolean destroyed = false;

	public Portal(int x, int y) {
		super(x, y);

		delayTicks = 1000;
	}

	/**
	 * function generates a monster at a specified place
	 * 
	 * @param x
	 * @param y
	 * @param spawnIsInRoom
	 */
	public static void generate(int x, int y) {
		new Portal(x, y);
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {

		if (destroyed) {
			tileFac.drawTile(gc, imgSourceDestroyed, (x - offsetX), (y - offsetY), size);
		} else {
			tileFac.drawTile(gc, imgSource, (x - offsetX), (y - offsetY), size);
		}
	}

	@Override
	public void tick(int ticks) {

		if (blocked >= 0) {
			blocked -= ticks;
		}

		if (blocked < 0 && !destroyed) {

			blocked = delayTicks;

			spawnMonster();

			if (intersectsWithPlayer() && !destroyed) {

				destroyed = true;

				// alert
				GameControl.getControl().alert("YKWTBI");

				// goal update
				GameControl.getControl().updateGoal(Goal.PORTAL);

			}
		}

	}

	private void spawnMonster() {

		int directionIndex = rnd.nextInt(Direction.values().length);

		Direction dir;
		Point spawnPoint;

		do {
			dir = Direction.values()[directionIndex];

			spawnPoint = dir.move(new Point(x, y));

			directionIndex = (++directionIndex) % Direction.values().length;

		} while (!GameControl.getControl().getMap().isWalkable(spawnPoint.x, spawnPoint.y));

		// let the generator generate the params ?
		if (maxMonsterSpawn > 0) {
			// generate monster parameter
			// MonsterType type =
			// MonsterType.values()[rnd.nextInt(MonsterType.values().length -
			// 1)];
			// int dmg = rnd.nextInt(3) + 1;

			// make monster at (x, y)
			boolean spawnIsInRoom = GameControl.getControl().getMap().isWalkableRoom(x, y);
			new Monster(x, y, spawnIsInRoom);
		}
	}

}
