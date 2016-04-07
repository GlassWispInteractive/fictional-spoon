package game.entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import framework.ScreenControl;
import game.combat.CombatEntity;
import game.walk_strategies.FloorWalk;
import game.walk_strategies.HorizontalWalk;
import game.walk_strategies.RandomWalk;
import game.walk_strategies.RectangleWalk;
import game.walk_strategies.VerticalWalk;
import game.walk_strategies.WalkStrategy;
import engine.ImageSource;
import engine.TileFactory;
import engine.TileSource;
import javafx.scene.canvas.GraphicsContext;
import screens.CombatScreen;

public class Monster extends CombatEntity {
	private ImageSource[] tileType = { new ImageSource(TileSource.MONSTER_TILES, 2, 5),
			new ImageSource(TileSource.MONSTER_TILES, 0, 5), new ImageSource(TileSource.MONSTER_TILES, 0, 1),
			new ImageSource(TileSource.MONSTER_TILES, 5, 4), new ImageSource(TileSource.MONSTER_TILES, 1, 8),
			new ImageSource(TileSource.MONSTER_TILES, 0, 7) };

	public enum MonsterType {
		EARTH, FIRE, AIR, WATER, MYSTIC
	};

	// class members
	private static TileFactory tileFac = TileFactory.getTilesFactory();
	private static int level;

	// object
	private ArrayList<WalkStrategy> walkStrategiesInRoom = new ArrayList<WalkStrategy>(Arrays.asList(
			new WalkStrategy[] { new RandomWalk(), new HorizontalWalk(), new VerticalWalk(), new RectangleWalk() }));
	private WalkStrategy currentWalkStrategy;

	// for speed
	private int blocked = 0;

	MonsterType type;
	int damage;
	private String name = "noname";

	public Monster(int x, int y, boolean spawnIsInRoom) {
		super(x, y, 2 + 1 * (level - 1), 1);

		// set walk strategy
		if (spawnIsInRoom) {
			int chosenStrategy = new Random().nextInt(walkStrategiesInRoom.size());
			this.currentWalkStrategy = walkStrategiesInRoom.get(chosenStrategy);
		} else {
			this.currentWalkStrategy = new FloorWalk();
		}

		// generate monster attribute values
		this.damage = 1 + level / 5;

		// this.name = name;
		this.delayTicks = 30;

		this.type = MonsterType.FIRE;
		// this.attack = new Attacks(damage);

	}

	/**
	 * set the level to generate monster by some hardness factor
	 * 
	 * @param level
	 */
	public static void setPower(int level) {
		Monster.level = level;
	}

	public String getName() {
		return name;
	}

	public String getHpInfo() {
		return "[" + life + " / " + maxLife + "]";
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {

		if (!isAlive()) {
			tileFac.drawTile(gc, tileType[tileType.length - 1], (x - offsetX), (y - offsetY), size);
		} else {
			tileFac.drawTile(gc, tileType[type.ordinal()], (x - offsetX), (y - offsetY), size);
		}
	}

	@Override
	public void tick(int ticks) {

		if (!isAlive()) {
			return;
		}

		// check intersection before AND after moving
		// check intersection
		if (intersectsWithPlayer()) {
			startCombat();
		}

		if (blocked >= 0) {
			blocked -= ticks;
		}

		if (isAlive()) {

			if (blocked <= 0) {
				blocked += delayTicks;

				Point newPosition = currentWalkStrategy.walk(x, y);

				x = newPosition.x;
				y = newPosition.y;
			}

			// check intersection
			if (intersectsWithPlayer()) {
				startCombat();
			}
		}

	}

	private void startCombat() {
		// goal update
		// moved to Combat class - stays here for debugging
//		GameControl.getControl().updateGoal(Goal.MONSTER);

		// game logic
//		life = 0; // debug
		ScreenControl.getCtrl().addScreen("combat", new CombatScreen(this));
		ScreenControl.getCtrl().setScreen("combat");
	}

	public ImageSource getImageSource() {
		return tileType[type.ordinal()];
	}
}