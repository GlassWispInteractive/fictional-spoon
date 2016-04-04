package entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import combat.Goal;
import engine.ImageSource;
import engine.TileFactory;
import engine.TileSource;
import entities.Monster.MonsterType;
import entities.WalkStrategies.FloorWalk;
import entities.WalkStrategies.HorizontalWalk;
import entities.WalkStrategies.RandomWalk;
import entities.WalkStrategies.RectangleWalk;
import entities.WalkStrategies.VerticalWalk;
import entities.WalkStrategies.WalkStrategy;
import framework.GameControl;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Opponent extends Entity {

	private TileFactory tileFac = TileFactory.getTilesFactory();
	private ImageSource imgSource = new ImageSource(TileSource.CHAR_TILES, 0, 11);
	private int blocked = 0;
	private boolean opponentDead = false;
	Random rnd = new Random();

	private ArrayList<WalkStrategy> walkStrategies = new ArrayList<WalkStrategy>(Arrays.asList(new WalkStrategy[] {
			new RandomWalk(), new HorizontalWalk(), new VerticalWalk(), new FloorWalk(), new RectangleWalk() }));
	private WalkStrategy currentWalkStrategy;

	private ArrayList<Monster> monsterList = new ArrayList<Monster>();

	public Opponent(int x, int y, String name) {
		super(x, y);

		this.delayTicks = 10;
		int chosenStrategy = new Random().nextInt(walkStrategies.size());
		this.currentWalkStrategy = walkStrategies.get(chosenStrategy);

		// hard coded monster
		for (int i = 0; i < 3; i++) {
		    	//generate monster parameter
			MonsterType type = MonsterType.values()[rnd.nextInt(MonsterType.values().length - 1)];
			int dmg = rnd.nextInt(3) + 1;

			// make monster at (x, y)
			boolean spawnIsInRoom = GameControl.getControl().getMap().isWalkableRoom(x, y);
			EntityFactory.getFactory().makeMonster(x, y, spawnIsInRoom, 100, type, dmg, "monster");
		}
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {

		if (opponentDead) {
			gc.setFill(Color.RED);
			gc.fillRect((x - offsetX) * size, (y - offsetY) * size, size, size);
			tileFac.drawTile(gc, imgSource, (x - offsetX), (y - offsetY), size);
		} else {
			tileFac.drawTile(gc, imgSource, (x - offsetX), (y - offsetY), size);
		}
	}

	@Override
	public void tick(int ticks) {

		if (!opponentDead) {

			if (blocked >= 0) {
				blocked--;
			}

			if (blocked < 0) {
				blocked = delayTicks - 1;

				Point newPosition = currentWalkStrategy.walk(x, y);

				x = newPosition.x;
				y = newPosition.y;
			}

			if (intersectsWithPlayer()) {
				// alert
				// GameControl.getControl().alert("New combo: ");

				// goal update
				GameControl.getControl().updateGoal(Goal.OPPONENT);

				// game logic
				opponentDead = true; // debug
				// ScreenControl.getCtrl().addScreen("combat", new
				// Combat(this));
				// ScreenControl.getCtrl().setScreen("combat");
			}

		}
	}

	public ArrayList<Monster> getMonsterList() {
		return monsterList;
	}

	public boolean isDead() {
		return opponentDead;
	}

	public void setDead(boolean dead) {
		this.opponentDead = dead;
	}

}
