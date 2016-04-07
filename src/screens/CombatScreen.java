package screens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import engine.TileFactory;
import framework.EventControl;
import framework.GameControl;
import framework.Global;
import framework.Screen;
import framework.ScreenControl;
import game.combat.BasicAttack;
import game.combat.CombatEntity;
import game.combat.ComboAttack;
import game.combat.Quest.Goal;
import game.entities.Monster;
import game.entities.Player;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.TextAlignment;

public class CombatScreen extends Screen {

	// vars for logic and controlling
	private ArrayList<BasicAttack> streak;

	// attributes
	private ArrayList<Monster> enemies;
	private int markAttack, markTarget;
	private double status, lowerBound, upperBound;
	private String info;

	// image array
	private static String[] attackName = new String[] { "Earth (1)", "Fire (2)", "Air (3)", "Water (4)" };
	private static final Image attackImg[] = new Image[] { new Image("/resources/elem/earth.png"),
			new Image("/resources/elem/fire.png"), new Image("/resources/elem/wind.png"),
			new Image("/resources/elem/water.png") };

	public CombatScreen(ArrayList<Monster> enemies) {
		super();

		this.info = "Use 1, 2, 3 or 4 to attack";

		this.enemies = enemies;
		this.markAttack = -1;
		this.markTarget = 0;

		this.streak = new ArrayList<>();
		setBounds();

		// enemy has one free action
		enemyAction();

		// design
		addLayer("elems", 0, Global.WINDOW_HEIGHT * 0.65, Global.WINDOW_WIDTH, 300);
		addLayer("monster", 0, 0, Global.WINDOW_WIDTH, 300);
		addLayer("bar", 0, Global.WINDOW_HEIGHT * 0.4, Global.WINDOW_WIDTH, 100);
		addLayer("info", 0, Global.WINDOW_HEIGHT * 0.3, Global.WINDOW_WIDTH, 100);
		addLayer("combo", 0, Global.WINDOW_HEIGHT - 510, Global.WINDOW_WIDTH, 510);

	}

	public CombatScreen(Monster enemy) {
		this(new ArrayList<Monster>(Arrays.asList(new Monster[] { enemy })));
	}

	@Override
	protected void tick(int ticks) {
		status = Math.min(1, status + ticks / 40.0);

		EventControl e = EventControl.getEvents();

		if (markTarget > enemies.size() - 1) {
			markTarget = enemies.size() - 1;
		}

		// arrow pressed
		if (e.isLeft()) {
			markTarget = (markTarget + 1) % enemies.size();
		}
		if (e.isRight()) {
			markTarget = (markTarget + enemies.size() - 1) % enemies.size();
		}

		// check if a number pressed
		if (e.isOne()) {
			usedAttack(0);
		}

		if (e.isTwo()) {
			usedAttack(1);
		}

		if (e.isThree()) {
			usedAttack(2);
		}

		if (e.isFour()) {
			usedAttack(3);
		}
		EventControl.getEvents().clear();

		// nothing pressed
		if (status == 1) {
			failedAttack();
		}

		// delete dead monster
		Iterator<Monster> i = enemies.iterator();
		while (i.hasNext()) {
			// get a monster from the iterator
			Monster mob = i.next();

			// finally delete if from list
			if (!mob.isAlive()) {
				i.remove();
			}
		}

		// check if all enemies are killed
		if (enemies.size() == 0) {
			// goal update
			GameControl.getControl().updateGoal(Goal.MONSTER);

			// set screens
			ScreenControl.getCtrl().removeScreen("combat");
			ScreenControl.getCtrl().setScreen("game");
		}
	}

	/**
	 * enemies attack the player
	 */
	private void enemyAction() {
		// enemies attack the players
		for (CombatEntity attackMonster : enemies) {
			attackMonster.attack(Player.getNewest());
		}

		// check if the player die
		if (!Player.getNewest().isAlive()) {
			ScreenControl.getCtrl().setScreen("game over");
		}
	}

	private void usedAttack(int id) {
		if (status < lowerBound || status > upperBound) {
			failedAttack();
		}

		// set visuals
		markAttack = id;
		streak.add(BasicAttack.values()[markAttack]);
		info = "current hit streak: " + ComboAttack.toString(streak.toArray(new BasicAttack[] {}));

		// execute attack
		Player.getNewest().attack(enemies.get(markTarget));
		evalStreakForCombo();

		// reset bar
		setBounds();
	}

	private void failedAttack() {
		markAttack = -1;
		streak.clear();
		info = "miss";

		// player gets attacked now
		enemyAction();

		// reset bar
		setBounds();
	}

	private void setBounds() {
		// set status to 0
		status = 0;

		if (streak.size() <= 1) {
			lowerBound = 0.5;
			upperBound = 0.9;
		} else if (streak.size() <= 4) {
			lowerBound = 0.6;
			upperBound = 0.8;
		} else if (streak.size() <= 7) {
			lowerBound = 0.65;
			upperBound = 0.75;
		} else if (streak.size() <= 9) {
			lowerBound = 0.68;
			upperBound = 0.75;
		} else {
			lowerBound = 0.70;
			upperBound = 0.75;
		}
	}

	private void evalStreakForCombo() {
		for (ComboAttack combo : ComboAttack.getCombosInUse()) {
			BasicAttack[] elements = combo.getCombo();
			if (streak.size() >= elements.length) {

				for (int i = 1; i <= elements.length; i++) {
					if (elements[elements.length - i] != streak.get(streak.size() - i)) {
						break;
					}
					if (i == elements.length) {
						info = "Combo completed!";
						Player.getNewest().attack(enemies.get(markTarget), elements.length);
						streak.clear();
					}
				}
			}
		}
	}

	@Override
	public void render() {
		// start from clean screen
		GraphicsContext gc = gcs.get("main");
		gc.clearRect(0, 0, Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT);

		renderElements();
		renderMonsters();
		renderBar();
		renderInfo();
		renderComboOverview();
	}

	/** 
	 * render the attacks
	 */
	private void renderElements() {
		// initialize render screen
		final GraphicsContext gc = gcs.get("elems");
		gc.clearRect(0, 0, layers.get("elems").getWidth(), layers.get("elems").getHeight());

		for (int i = 0; i < attackName.length; i++) {
			gc.setFill(Global.WHITE);
			gc.fillText(attackName[i], 80 + i * 180, 25, 80);

			gc.drawImage(attackImg[i], 50 + i * 180, 50, attackImg[i].getWidth() / 3, attackImg[i].getHeight() / 3);

			// gc.fillRect(50 + i*120, height * 0.65, 80, 80);

			if (markAttack >= 0) {
				gc.setStroke(Global.WHITE);
				gc.setLineWidth(3);
				gc.strokeRect(50 + markAttack * 180, 50, attackImg[markAttack].getWidth() / 3,
						attackImg[markAttack].getHeight() / 3);
			}
		}
		
		// render the player name and status
		gc.setFont(Global.DEFAULT_FONT);
		gc.setTextBaseline(VPos.BASELINE);
		gc.fillText(Player.getNewest().toString(), 50, 230);
	}

	/**
	 * render the monsters
	 */
	private void renderMonsters() {
		// initialize render screen
		final GraphicsContext gc = gcs.get("monster");
		gc.clearRect(0, 0, layers.get("monster").getWidth(), layers.get("monster").getHeight());

		for (int i = 0; i < enemies.size(); i++) {
			Monster enemy = enemies.get(i);

			Image image = TileFactory.getTilesFactory().getImage(enemy.getImageSource());

			gc.setFill(Global.WHITE);
			gc.fillText("no name" + " " + enemy.getLife() + " / " + enemy.getMaxLife(),
					Global.WINDOW_WIDTH - 180 - i * 180 - image.getWidth(), 50 - 5, 130);

			gc.drawImage(image, Global.WINDOW_WIDTH - 180 - i * 180 - image.getWidth(), 50, 130, 130);
			// gc.fillRect(Window.SIZE_X - 150 - i * 120, 50, 80, 80);

			if (markTarget % enemies.size() == i) {
				gc.setStroke(Global.WHITE);
				gc.setLineWidth(4);
				gc.strokeRect(Global.WINDOW_WIDTH - 180 - i * 180 - image.getWidth(), 50, 130, 130);
			}
		}

	}

	/**
	 * render the progress bar
	 */
	private void renderBar() {
		// initialize render screen
		final GraphicsContext gc = gcs.get("bar");
		gc.clearRect(0, 0, layers.get("bar").getWidth(), layers.get("bar").getHeight());

		// settings
		final int magin = 110, width = Global.WINDOW_WIDTH - 2 * magin;

		// big box
		gc.setLineWidth(3);
		gc.setStroke(Global.WHITE.darker());
		gc.strokeRoundRect(magin - 10, 5, width + 20, 30, 30, 30);

		// draw the delimters
		gc.strokeLine(magin + lowerBound * width - 3, 5, magin + lowerBound * width - 3, 35);
		gc.strokeLine(magin + upperBound * width - 3, 5, magin + upperBound * width - 3, 35);

		// small box - the progress bar
		gc.setFill(Global.ORANGE);
		// +5 to be lower than outer rect
		// +2 to have the left border over the delimeter
		gc.fillRoundRect(magin, 10, status * width, 20, 20, 20);

	}

	/**
	 * render the info label above the progress bar
	 */
	private void renderInfo() {
		// initialize render screen
		final GraphicsContext gc = gcs.get("info");
		gc.clearRect(0, 0, layers.get("info").getWidth(), layers.get("info").getHeight());

		// font settings
		gc.setFont(Global.DEFAULT_FONT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.BASELINE);

		// print out current success
		gc.setFill(Global.ORANGE);
		// gc.setLineWidth(1);

		gc.fillText(info, Global.WINDOW_WIDTH / 2, 50);

		// gc.strokeText(pointsText, 360, Window.SIZE_Y * 0.3);

	}

	/**
	 * render a legend for the learned combos
	 */
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
		for (ComboAttack combo : ComboAttack.getCombosInUse()) {
			comboNames.add(combo.toString());
		}
		if (ComboAttack.getCombosInUse().size() == 0) {
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

		for (int j = 0; j < Math.min(9, comboNames.size()); j++) {

			// only max 10 combos can be shown

			gc.setStroke(Global.ORANGE);
			gc.strokeRect(columnX, rowY, padding + width, height);

			gc.setFill(Global.ORANGE);
			gc.fillText(comboNames.get(j).toString(), columnX + width / 2, rowY + height / 2 + textHeight / 4);

			rowY += height;
		}
	}
}
