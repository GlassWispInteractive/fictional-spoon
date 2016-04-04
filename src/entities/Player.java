package entities;

import java.util.ArrayList;
import java.util.Arrays;

import combat.Combat;
import combat.Combo;
import combat.IAttackable;
import combat.Soul;
import engine.ImageSource;
import engine.TileFactory;
import engine.TileSource;
import framework.EventControl;
import framework.GameControl;
import generation.Map;
import javafx.scene.canvas.GraphicsContext;

public class Player extends Entity implements IAttackable {

	private int maxHp = 100, hp;
	private String name = "Spieler";
	private Combat combat;

	private ArrayList<Soul> souls = new ArrayList<Soul>(Arrays.asList(
			new Soul[] { new Soul("Earth (1)"), new Soul("Fire (2)"), new Soul("Air (3)"), new Soul("Water (4)") }));

	// for speed
	private int blocked = 0;

	TileFactory tileFac;

	public Player(int x, int y) {
		super(x, y);
		delayTicks = 4;
		this.hp = 20;

		tileFac = TileFactory.getTilesFactory();
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {

		ImageSource imgSource = new ImageSource(TileSource.CHAR_TILES, 0, 8);
		tileFac.drawTile(gc, imgSource, (x - offsetX), (y - offsetY), size);
	}

	@Override
	public void tick(int ticks) {
		EventControl e = EventControl.getEvents();
		int newX = x, newY = y;
		boolean moved = false;

		if (blocked >= 0) {
			blocked -= ticks;
		}

		if (e.isLeft()) {
			newX--;
			moved = true;
		} else if (e.isRight()) {
			newX++;
			moved = true;
		} else if (e.isUp()) {
			newY--;
			moved = true;
		} else if (e.isDown()) {
			newY++;
			moved = true;
		}

		Map map = GameControl.getControl().getMap();

		if (moved && map.isWalkable(newX, newY) && blocked <= 0) {

			blocked = delayTicks;

			x = newX;
			y = newY;

			GameControl.getControl().updateCamera(x, y);
		}

	}
	
	/**
	 * @return the hp
	 */
	public int getHp() {
		return hp;
	}

	public String getPlayerInfo() {
		return "" + name + " [" + hp + " / " + maxHp + "]";
	}

	public boolean isDead() {
		return hp <= 0;
	}

	public ArrayList<Soul> getSouls() {
		return souls;
	}

	public void setCombat(Combat combat) {
		this.combat = combat;
	}

	public void heal() {
		this.hp = maxHp;
	}

	@Override
	public void getDmg(int dmg) {
		hp -= dmg;
	}

	@Override
	public void doAttack(IAttackable focus) {
		souls.get(combat.getCurSoul()).getAttack().doAttack(focus);
	}

	@Override
	public void doAttack(IAttackable focus, Combo combo) {
		souls.get(combat.getCurSoul()).getAttack().doAttack(focus, combo);
	}
}
