package combat;

import java.util.ArrayList;
import java.util.Arrays;

import entities.EntityFactory;
import entities.Monster;
import game.Events;
import game.GameScene;
import game.TileFactory;
import game.Window;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class Combat extends GameScene {

	private ArrayList<Soul> souls;
	private ArrayList<Monster> monster;

	private int curSoul = 0;
	private int curFocus = 0;
	private int curAttackRow = 0;
	private int curAttackColum = 0;
	private double status, lowerBound, upperBound;
	private int streakCount;
	private ArrayList<Element> streak;
	private String info;

	private enum CombatState {
		CHOOSE_SOUL, CHOOSE_ATTACK, CHOOSE_FOCUS
	}

	private CombatState combatState = CombatState.CHOOSE_SOUL;

	private final Image ELEMS[] = new Image[] { new Image("/resources/elem/earth.png"),
			new Image("/resources/elem/fire.png"), new Image("/resources/elem/wind.png"),
			new Image("/resources/elem/water.png") };
	// private final Image FIRE = new Image("/resources/elem/fire.png");
	// private final Image WIND = new Image("/resources/elem/wind.png");
	// private final Image WATER = new Image("/resources/elem/water.png");

	public Combat() {
		super();

		// inits
		curSoul = 0;
		curFocus = 0;
		curAttackRow = 0;
		curAttackColum = 0;

		status = 0;
		lowerBound = 0.6;
		upperBound = 0.75;
		streakCount = 0;

		info = "Use 1, 2, 3 or 4 to attack";
		streak = new ArrayList<>();

		// add layers
		addLayer(new Canvas(Window.SIZE_X, 300));
		addLayer(new Canvas(Window.SIZE_X, 300));
		addLayer(new Canvas(Window.SIZE_X, 100));
		addLayer(new Canvas(Window.SIZE_X, 100));

		// put up design
		
		layers.get(1).relocate(0, Window.SIZE_Y * 0.65);
		layers.get(2).relocate(0, 0);
		layers.get(3).relocate(0, Window.SIZE_Y * 0.4);
		layers.get(4).relocate(0, Window.SIZE_Y * 0.3);

		// hardcoded stuff
		ComboFactory.getFac().makeCombo(new Element[] { Element.EARTH, Element.FIRE });
		ComboFactory.getFac().makeCombo(new Element[] { Element.WATER, Element.WATER, Element.AIR });
		ComboFactory.getFac()
				.makeCombo(new Element[] { Element.EARTH, Element.FIRE, Element.AIR, Element.EARTH, Element.WATER });

		setSouls(getHardCodedSouls());
		setMonster(getHardCodedMonster());

	}

	private static ArrayList<Monster> getHardCodedMonster() {
		ArrayList<Monster> list = new ArrayList<Monster>();

		list.add(EntityFactory.getFactory().makeMonster(13, 13, 0, new int[] { 1, 2, 3, 4, 5 }, "name"));
		list.add(EntityFactory.getFactory().makeMonster(12, 23, 0, new int[] { 1, 6, 3, 4, 5 }, "test"));
		list.add(EntityFactory.getFactory().makeMonster(14, 33, 0, new int[] { 1, 2, 3, 4, 2 }, "rudolf"));

		return list;
	}

	private static ArrayList<Soul> getHardCodedSouls() {
		ArrayList<Soul> list = new ArrayList<Soul>();

		list.add(new Soul("Earth (1)"));
		list.add(new Soul("Fire (2)"));
		list.add(new Soul("Air (3)"));
		list.add(new Soul("Water (4)"));

		return list;
	}

	private void setSouls(ArrayList<Soul> souls) {
		this.souls = souls;
	}

	private void setMonster(ArrayList<Monster> monster) {
		this.monster = monster;
	}

	public void tick(double ticks) {
		status = Math.min(1, status + ticks / 120);

		Events e = Events.getEvents();

		// if (e.isLeft()) {
		// curSoul = (curSoul + souls.size() - 1) % souls.size();
		// }
		// if (e.isRight()) {
		// curSoul = (curSoul + 1) % souls.size();
		// }
		//

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

		// case CHOOSE_ATTACK:
		// if (e.isLeft()) {
		// if (curAttackRow <= 1) {
		// curAttackColum = Math.max(--curAttackColum, 0);
		// } else {
		// curAttackColum = Math.max(--curAttackColum, 1);
		// }
		// }
		// if (e.isRight()) {
		// curAttackColum = Math.min(++curAttackColum, 1);
		// }
		// if (e.isUp()) {
		// curAttackRow = Math.max(--curAttackRow, 0);
		// }
		// if (e.isDown()) {
		// if (curAttackColum == 0) {
		// curAttackRow = Math.min(++curAttackRow, 1);
		// } else {
		// curAttackRow = Math.min(++curAttackRow, 2);
		// }
		// }
		// if (e.isEnter()) {
		// if (curAttackRow == 2 && curAttackColum == 1) {
		// // back buton
		// combatState = CombatState.CHOOSE_SOUL;
		// } else {
		// // choose attack focus
		// combatState = CombatState.CHOOSE_FOCUS;
		// }
		// }
		// break;
		//
		// case CHOOSE_FOCUS:
		// if (e.isLeft()) {
		// curFocus = (curFocus + 1) % monster.size();
		// }
		// if (e.isRight()) {
		// curFocus = (curFocus + monster.size() - 1) % monster.size();
		// }
		// if (e.isEnter()) {
		// // attack
		// // TODO attack monster
		// }
		// break;

		Events.getEvents().clear();

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

	private void attack() {
		// check whether timing is fine
		if (status > lowerBound && status < upperBound) {
			streakCount++;

			streak.add(Element.values()[curSoul]);
			info = "current hit streak: " + streak.toString();

			// System.out.println("Bonus damage");
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

	public void eval(Element[] streakElem) {
		// System.out.println(Arrays.toString(combo));

		for (Combo combo : ComboFactory.getFac().getCombos()) {
			if (Arrays.equals(streakElem, combo.getCombo())) {
				info = "Combo completed!";
				streak.clear();
			}
		}

	}

	public void render() {
		// start from clean screen
		GraphicsContext gc = gcs.get(0);
		gc.clearRect(0, 0, Window.SIZE_X, Window.SIZE_Y);

		renderElements();
		renderMonsters();
		renderBar();
		renderInfo();

		// int textboxWidth = 600;
		// int textboxHeight = 240;
		// renderTextboxes(gc, (int) (width - textboxWidth), (int) (height -
		// textboxHeight), textboxWidth, textboxHeight,
		// souls.get(curSoul));

	}

	private void renderElements() {
		// initialize render screen
		final int ID = 1;
		final GraphicsContext gc = gcs.get(ID);
		gc.clearRect(0, 0, layers.get(ID).getWidth(), layers.get(ID).getHeight());
		
		for (int i = 0; i < souls.size(); i++) {
			gc.setFill(Color.ANTIQUEWHITE);
			gc.fillText(souls.get(i).getName(), 80 + i * 180, 25, 80);

			gc.drawImage(ELEMS[i], 50 + i * 180, 50, ELEMS[i].getWidth() / 3,
					ELEMS[i].getHeight() / 3);

			// gc.fillRect(50 + i*120, height * 0.65, 80, 80);

			if (curSoul == i && streakCount > 0) {
				gc.setStroke(Color.ANTIQUEWHITE);
				gc.setLineWidth(3);
				gc.strokeRect(50 + i * 180, 50, ELEMS[i].getWidth() / 3, ELEMS[i].getHeight() / 3);
			}
		}
	}

	private void renderMonsters() {
		// initialize render screen
		final int ID = 2;
		final GraphicsContext gc = gcs.get(ID);
		gc.clearRect(0, 0, layers.get(ID).getWidth(), layers.get(ID).getHeight());

		for (int i = 0; i < monster.size(); i++) {

			Image image = TileFactory.getTilesFactory().getImage(monster.get(i).getImageSource());

			gc.setFill(Color.RED);
			gc.fillText(monster.get(i).getName(), Window.SIZE_X - 150 - i * 180 - image.getWidth(), 50 - 5, 80);

			gc.drawImage(image, Window.SIZE_X - 180 - i * 180 - image.getWidth(), 50, 130, 130);
			// gc.fillRect(Window.SIZE_X - 150 - i * 120, 50, 80, 80);

			if (curFocus % monster.size() == i) {
				gc.setStroke(Color.RED);
				gc.setLineWidth(4);
				gc.strokeRect(Window.SIZE_X - 180 - i * 180 - image.getWidth(), 50, 130, 130);
			}
		}

	}

	private void renderBar() {
		// initialize render screen
		final int ID = 3;
		final GraphicsContext gc = gcs.get(ID);
		gc.clearRect(0, 0, layers.get(ID).getWidth(), layers.get(ID).getHeight());

		// fancy line at 40%
		gc.setLineWidth(3);
		gc.setStroke(Color.GREY);
		gc.strokeRoundRect(100, 5, Window.SIZE_X - 200, 30, 30, 30);

		// progress bar
		gc.setFill(Color.ORANGE);
		// +5 to be lower than outer rect
		// +2 to have the left border over the delimeter
		gc.fillRoundRect(110, 10, status * (Window.SIZE_X - 220) + 2, 20, 20, 20);

		// draw the delimters
		gc.setStroke(Color.GREY);
		gc.strokePolyline(
				new double[] { 110 + lowerBound * (Window.SIZE_X - 220), 110 + lowerBound * (Window.SIZE_X - 220) },
				new double[] { 5, 35 }, 2);

		gc.strokePolyline(
				new double[] { 110 + upperBound * (Window.SIZE_X - 220), 110 + upperBound * (Window.SIZE_X - 220) },
				new double[] { 5, 35 }, 2);
	}

	private void renderInfo() {
		// initialize render screen
		final int ID = 4;
		final GraphicsContext gc = gcs.get(ID);
		gc.clearRect(0, 0, layers.get(ID).getWidth(), layers.get(ID).getHeight());

		// font settings
		gc.setFont(Window.bigFont);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.BASELINE);

		// print out current success
		gc.setFill(Color.ORANGE);
		// gc.setLineWidth(1);

		gc.fillText(info, Window.SIZE_X/2, 50);

		// gc.strokeText(pointsText, 360, Window.SIZE_Y * 0.3);

	}

	@SuppressWarnings("unused")
	private void renderTextboxes(GraphicsContext gc, int x, int y, int width, int height, Soul currentSoul) {

		int rowY = y;
		int columX = x;

		Attacks[][] attacks = currentSoul.getAttacks();

		// attack fields
		for (int i = 0; i < attacks.length; i++) {

			columX = x;

			for (int j = 0; j < attacks[i].length; j++) {

				int boxWidth = width / attacks.length;
				int boxHeight = height / (attacks[i].length + 1);

				gc.setFill(Color.WHITE);
				if (combatState == CombatState.CHOOSE_ATTACK && curAttackRow == i && curAttackColum == j) {
					gc.setFill(Color.GRAY);
				}
				gc.fillRect(columX, rowY, boxWidth, boxHeight);

				gc.setStroke(Color.BLACK);
				gc.strokeRect(columX, rowY, boxWidth, boxHeight);
				gc.setFill(Color.BLACK);
				gc.fillText(attacks[i][j].getName(), columX, rowY + boxHeight / 2);

				columX += width / attacks.length;
			}

			rowY += height / (attacks[i].length + 1);
		}

		// back button
		gc.setFill(Color.WHITE);
		if (combatState == CombatState.CHOOSE_ATTACK && curAttackRow == attacks.length
				&& curAttackColum == attacks[attacks.length - 1].length - 1) {
			gc.setFill(Color.GRAY);
		}
		gc.fillRect(columX - width / attacks.length, rowY, width / attacks.length,
				height / (attacks[attacks.length - 1].length + 1));

		gc.setStroke(Color.BLACK);
		gc.strokeRect(columX - width / attacks.length, rowY, width / attacks.length,
				height / (attacks[attacks.length - 1].length + 1));
		gc.setFill(Color.BLACK);
		gc.fillText("BACK", columX - width / attacks.length,
				rowY + height / (attacks[attacks.length - 1].length + 1) / 2);
	}

}
