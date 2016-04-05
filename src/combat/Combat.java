package combat;

import java.util.ArrayList;
import java.util.Arrays;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;

import engine.TileFactory;
import entities.Monster;
import entities.Opponent;
import entities.Player;
import framework.EventControl;
import framework.GameControl;
import framework.Global;
import framework.Screen;
import framework.ScreenControl;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class Combat extends Screen {
	// lists
	private String[] attacks;
	private ArrayList<Monster> monster;
	private Opponent opponent = null; // null = only monster
	private Player player = Player.getNewest();

	// class member
	private int curSoul, curFocus;
	@SuppressWarnings("unused")
	private int curAttackRow, curAttackColum;
	private int streakCount;
	private ArrayList<Element> streak;
	private double status, lowerBound, upperBound;
	private String info;
	@SuppressWarnings("unused")
	private CombatState combatState;

	private int delayTicks = 100;
	private int blocked = delayTicks;

	// internal states
	private enum CombatState {
		CHOOSE_SOUL, CHOOSE_ATTACK, CHOOSE_FOCUS
	}

	// image array
	private final Image ELEMS[] = new Image[] { new Image("/resources/elem/earth.png"),
			new Image("/resources/elem/fire.png"), new Image("/resources/elem/wind.png"),
			new Image("/resources/elem/water.png") };

	public Combat(Opponent opponent) {
		this(new ArrayList<Monster>(opponent.getMonsterList()));

		this.opponent = opponent;
	}

	public Combat(Monster[] monsterArray) {
		this(new ArrayList<Monster>(Arrays.asList(monsterArray)));
	}

	public Combat(ArrayList<Monster> monster) {
		super();

		// inits
		info = "Use 1, 2, 3 or 4 to attack";
		streak = new ArrayList<>();

		addLayer("elems", 0, Global.WINDOW_HEIGHT * 0.65, Global.WINDOW_WIDTH, 300);
		addLayer("monster", 0, 0, Global.WINDOW_WIDTH, 300);
		addLayer("bar", 0, Global.WINDOW_HEIGHT * 0.4, Global.WINDOW_WIDTH, 100);
		addLayer("info", 0, Global.WINDOW_HEIGHT * 0.3, Global.WINDOW_WIDTH, 100);
		addLayer("info2", 0, Global.WINDOW_HEIGHT * 0.85, Global.WINDOW_WIDTH, 100);
		addLayer("combo", 0, Global.WINDOW_HEIGHT - 510, Global.WINDOW_WIDTH, 510);

		this.attacks = new String[] { "Earth (1)", "Fire (2)", "Air (3)", "Water (4)" };

		this.monster = monster;

	}

	@Override
	public void tick(int ticks) {
		// SpookingSouls.getObject().tick(ticks);

		status = Math.min(1, status + ticks / 40.0);
		// System.out.println(status);

		EventControl e = EventControl.getEvents();

		if (curFocus > monster.size() - 1) {
			curFocus = monster.size() - 1;
		}

		// arrow pressed
		if (e.isLeft()) {
			curFocus = (curFocus + 1) % monster.size();
		}
		if (e.isRight()) {
			curFocus = (curFocus + monster.size() - 1) % monster.size();
		}

		// number pressed
		if (e.isOne()) {
			curSoul = 0;
			attack();
		}

		if (e.isTwo()) {
			curSoul = 1;
			attack();
		}

		if (e.isThree()) {
			curSoul = 2;
			attack();
		}

		if (e.isFour()) {
			curSoul = 3;
			attack();
		}

		// nothing pressed
		if (status == 1) {
			attack();
		}

		setBounds();

		EventControl.getEvents().clear();

		// check if all monsters are still alive and make smartDelete with dead
		// monster
		ArrayList<Monster> dyingMonster = new ArrayList<Monster>();
		for (Monster mon : monster) {
			if (!mon.isAlive()) {
				dyingMonster.add(mon);
			}
		}
		// soft delete
		for (Monster dyingMon : dyingMonster) {
			monster.remove(dyingMon);
		}
		dyingMonster.clear();

		// check, if alive monster exists
		if (monster.size() == 0) {
			// opponent dead
			if (opponent != null) {
				opponent.setDead(true);
			}

			// goal update
			GameControl.getControl().updateGoal(Goal.MONSTER);

			// set screens
			ScreenControl.getCtrl().removeScreen("combat");
			ScreenControl.getCtrl().setScreen("game");
		}

		if (!player.isAlive()) {
			ScreenControl.getCtrl().setScreen("game over");
		}

		// let all monster attack
		if (blocked < 0) {
			blocked = delayTicks - 1;

			for (Monster attackMonster : monster) {
				attackMonster.attack(player);
			}
		} else {
			blocked--;
		}
	}

	private void setBounds() {
		if (streakCount <= 1) {
			lowerBound = 0.5;
			upperBound = 0.9;
		} else if (streakCount <= 4) {
			lowerBound = 0.6;
			upperBound = 0.8;
		} else if (streakCount <= 7) {
			lowerBound = 0.65;
			upperBound = 0.75;
		} else if (streakCount <= 9) {
			lowerBound = 0.68;
			upperBound = 0.75;
		} else {
			lowerBound = 0.70;
			upperBound = 0.75;
		}
	}

	public int getCurSoul() {
		return curSoul;
	}

	private void attack() {
		// check whether timing is fine
		if (status > lowerBound && status < upperBound) {
			streakCount++;

			streak.add(Element.values()[curSoul]);
			info = "current hit streak: " + Combo.toString(streak.toArray(new Element[] {}));

			player.attack(monster.get(curFocus));
		} else {
			// adjust level
			streakCount = 0;

			//
			streak.clear();
			info = "miss";

		}

		eval(streak.toArray(new Element[] {}));

		// reset bar progress
		status = 0;
	}

	private void eval(Element[] streakElem) {

		for (Combo combo : Combo.getCombosInUse()) {
			Element[] elements = combo.getCombo();
			if (streakElem.length >= elements.length) {
				for (int i = 1; i <= elements.length; i++) {
					if (elements[elements.length - i] != streakElem[streakElem.length - i]) {
						break;
					}
					if (i == elements.length) {
						info = "Combo completed!";
						player.attack(monster.get(curFocus), elements.length);
						streak.clear();
					}
				}
			}
		}

	}

	public void render() {
		// start from clean screen
		GraphicsContext gc = gcs.get("main");
		gc.clearRect(0, 0, Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT);

		// SpookingSouls.getObject().render(gc);

		renderElements();
		renderMonsters();
		renderBar();
		renderInfo();
		renderPlayerInfo();
		renderComboOverview();
	}

	private void renderElements() {
		// initialize render screen
		final GraphicsContext gc = gcs.get("elems");
		gc.clearRect(0, 0, layers.get("elems").getWidth(), layers.get("elems").getHeight());

		for (int i = 0; i < attacks.length; i++) {
			gc.setFill(Global.WHITE);
			gc.fillText(attacks[i], 80 + i * 180, 25, 80);

			gc.drawImage(ELEMS[i], 50 + i * 180, 50, ELEMS[i].getWidth() / 3, ELEMS[i].getHeight() / 3);

			// gc.fillRect(50 + i*120, height * 0.65, 80, 80);

			if (curSoul == i && streakCount > 0) {
				gc.setStroke(Global.WHITE);
				gc.setLineWidth(3);
				gc.strokeRect(50 + i * 180, 50, ELEMS[i].getWidth() / 3, ELEMS[i].getHeight() / 3);
			}
		}
	}

	private void renderMonsters() {
		// initialize render screen
		final GraphicsContext gc = gcs.get("monster");
		gc.clearRect(0, 0, layers.get("monster").getWidth(), layers.get("monster").getHeight());

		for (int i = 0; i < monster.size(); i++) {

			Image image = TileFactory.getTilesFactory().getImage(monster.get(i).getImageSource());

			gc.setFill(Global.RED);
			gc.fillText(monster.get(i).getName() + " " + monster.get(i).getHpInfo(),
					Global.WINDOW_WIDTH - 180 - i * 180 - image.getWidth(), 50 - 5, 130);

			gc.drawImage(image, Global.WINDOW_WIDTH - 180 - i * 180 - image.getWidth(), 50, 130, 130);
			// gc.fillRect(Window.SIZE_X - 150 - i * 120, 50, 80, 80);

			if (curFocus % monster.size() == i) {
				gc.setStroke(Global.RED);
				gc.setLineWidth(4);
				gc.strokeRect(Global.WINDOW_WIDTH - 180 - i * 180 - image.getWidth(), 50, 130, 130);
			}
		}

	}

	private void renderBar() {
		// initialize render screen
		final GraphicsContext gc = gcs.get("bar");
		gc.clearRect(0, 0, layers.get("bar").getWidth(), layers.get("bar").getHeight());

		// fancy line at 40%
		gc.setLineWidth(3);
		gc.setStroke(Color.GREY);
		gc.strokeRoundRect(100, 5, Global.WINDOW_WIDTH - 200, 30, 30, 30);

		// progress bar
		gc.setFill(Color.ORANGE);
		// +5 to be lower than outer rect
		// +2 to have the left border over the delimeter
		gc.fillRoundRect(110, 10, status * (Global.WINDOW_WIDTH - 220) + 2, 20, 20, 20);

		// draw the delimters
		gc.setStroke(Global.DARKGRAY.brighter());
		gc.strokePolyline(new double[] { 110 + lowerBound * (Global.WINDOW_WIDTH - 220),
				110 + lowerBound * (Global.WINDOW_WIDTH - 220) }, new double[] { 5, 35 }, 2);

		gc.strokePolyline(new double[] { 110 + upperBound * (Global.WINDOW_WIDTH - 220),
				110 + upperBound * (Global.WINDOW_WIDTH - 220) }, new double[] { 5, 35 }, 2);
	}

	private void renderInfo() {
		// initialize render screen
		final GraphicsContext gc = gcs.get("info");
		gc.clearRect(0, 0, layers.get("info").getWidth(), layers.get("info").getHeight());

		// font settings
		gc.setFont(Global.DEFAULT_FONT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.BASELINE);

		// print out current success
		gc.setFill(Color.ORANGE);
		// gc.setLineWidth(1);

		gc.fillText(info, Global.WINDOW_WIDTH / 2, 50);

		// gc.strokeText(pointsText, 360, Window.SIZE_Y * 0.3);

	}

	private void renderPlayerInfo() {
		// initialize render screen
		final GraphicsContext gc = gcs.get("info2");
		gc.clearRect(0, 0, layers.get("info2").getWidth(), layers.get("info2").getHeight());

		// font settings
		gc.setFont(Global.DEFAULT_FONT);
		// gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.BASELINE);
		gc.setFill(Color.ORANGE);
		// gc.setLineWidth(1);

		gc.fillText(player.toString(), 50, 50);
	}

	private void renderComboOverview() {
		// initialize render screen
		final GraphicsContext gc = gcs.get("combo");
		gc.clearRect(0, 0, layers.get("combo").getWidth(), layers.get("combo").getHeight());

		// font settings
		gc.setFont(Global.DEFAULT_FONT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.BASELINE);
		// gc.setLineWidth(1);

		ArrayList<String> comboNames = new ArrayList<String>();
		comboNames.add("Combos:");
		for (Combo combo : Combo.getCombosInUse()) {
			comboNames.add(combo.toString());
		}
		if (Combo.getCombosInUse().size() == 0) {
			comboNames.clear();
			comboNames.add("No Combos");
		}

		FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
		int textWidth = (int) fontLoader.computeStringWidth("", gc.getFont());
		int textHeight = (int) fontLoader.getFontMetrics(gc.getFont()).getLineHeight();

		// calc textLength
		for (int i = 0; i < comboNames.size(); i++) {
			if (textWidth < (int) fontLoader.computeStringWidth(comboNames.get(i).toString(), gc.getFont())) {
				textWidth = (int) fontLoader.computeStringWidth(comboNames.get(i).toString(), gc.getFont());
			}
		}

		int padding = 10;
		int width = textWidth + 2 * padding;
		int height = (int) (1.5 * textHeight);
		int rowY = (int) (layers.get("combo").getHeight() - height * Math.min(9, comboNames.size()) - padding);
		int columnX = (int) (layers.get("combo").getWidth() - width - 2 * padding);

		for (int j = 0; j < Math.min(9, comboNames.size()); j++) { // only max
																	// 10 combos
																	// can be
																	// shown

			gc.setStroke(Color.ORANGE);
			gc.strokeRect(columnX, rowY, padding + width, height);

			gc.setFill(Color.ORANGE);
			gc.fillText(comboNames.get(j).toString(), columnX + width / 2, rowY + height / 2 + textHeight / 4);

			rowY += height;
		}
	}
}
