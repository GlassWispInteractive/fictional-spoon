package combat;

import java.util.ArrayList;
import java.util.Arrays;

import entities.EntityFactory;
import entities.Monster;
import game.Events;
import game.TileFactory;
import game.Window;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Combat {

	private static Combat singleton;

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

	public static Combat startCombat(ArrayList<Soul> souls, ArrayList<Monster> monster) {
		if (singleton == null) {
			singleton = new Combat();

			singleton.setSouls(getHardCodedSouls());
			singleton.setMonster(getHardCodedMonster());
			ComboFactory.getFac().makeCombo(new Element[] { Element.EARTH, Element.FIRE });
			ComboFactory.getFac().makeCombo(new Element[] { Element.WATER, Element.WATER, Element.AIR });
			ComboFactory.getFac().makeCombo(
					new Element[] { Element.EARTH, Element.FIRE, Element.AIR, Element.EARTH, Element.WATER });
		}

		return singleton;
	}

	private Combat() {
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

	public void render(GraphicsContext gc) {
		// font settings
		Font font = Font.font("Helvetica", FontWeight.NORMAL, 16);
		gc.setFont(font);
		gc.setTextAlign(TextAlignment.LEFT);
		gc.setTextBaseline(VPos.BASELINE);

		// print out current success
		gc.setFill(Color.ORANGE);
		// gc.setLineWidth(1);

		gc.fillText(info, 360, Window.SIZE_Y * 0.3);
		// gc.strokeText(pointsText, 360, Window.SIZE_Y * 0.3);

		// // background
		// gc.setFill(Paint.valueOf("#C0C0C0"));
		// gc.fillRect(0, 0, width, height);

		// fancy line at 40%
		gc.setLineWidth(3);
		gc.setStroke(Color.GREY);
		gc.strokeRoundRect(100, Window.SIZE_Y * 0.4, Window.SIZE_X - 200, 30, 30, 30);

		// progress bar
		gc.setFill(Color.ORANGE);
		gc.fillRoundRect(110, Window.SIZE_Y * 0.4 + 5, status * (Window.SIZE_X - 220), 20, 20, 20);

		// draw the delimters
		gc.setStroke(Color.GREY);
		gc.strokePolyline(
				new double[] { 110 + lowerBound * (Window.SIZE_X - 220), 110 + lowerBound * (Window.SIZE_X - 220) },
				new double[] { Window.SIZE_Y * 0.4, Window.SIZE_Y * 0.4 + 30 }, 2);

		gc.strokePolyline(
				new double[] { 110 + upperBound * (Window.SIZE_X - 220), 110 + upperBound * (Window.SIZE_X - 220) },
				new double[] { Window.SIZE_Y * 0.4, Window.SIZE_Y * 0.4 + 30 }, 2);

		// souls at 65% height
		for (int i = 0; i < souls.size(); i++) {
			gc.setFill(Color.ANTIQUEWHITE);
			gc.fillText(souls.get(i).getName(), 80 + i * 180, Window.SIZE_Y * 0.65 - 5, 80);

			gc.drawImage(ELEMS[i], 50 + i * 180, Window.SIZE_Y * 0.65, ELEMS[i].getWidth() / 3,
					ELEMS[i].getHeight() / 3);

			// gc.fillRect(50 + i*120, height * 0.65, 80, 80);

			if (curSoul == i && streakCount > 0) {
				gc.setStroke(Color.ANTIQUEWHITE);
				gc.setLineWidth(3);
				gc.strokeRect(50 + i * 180, Window.SIZE_Y * 0.65, ELEMS[i].getWidth() / 3, ELEMS[i].getHeight() / 3);
			}
		}

		// monster at 10% height
		for (int i = 0; i < monster.size(); i++) {

			Image image = TileFactory.getTilesFactory().getImage(monster.get(i).getImageSource());

			gc.setFill(Color.RED);
			gc.fillText(monster.get(i).getName(), Window.SIZE_X - 150 - i * 180 - image.getWidth(),
					Window.SIZE_Y * 0.1 - 5, 80);

			gc.drawImage(image, Window.SIZE_X - 180 - i * 180 - image.getWidth(), Window.SIZE_Y * 0.1, 130, 130);
			// gc.fillRect(Window.SIZE_X - 150 - i * 120, Window.SIZE_Y * 0.1,
			// 80, 80);

			if (curFocus % monster.size() == i) {
				gc.setStroke(Color.RED);
				gc.setLineWidth(4);
				gc.strokeRect(Window.SIZE_X - 180 - i * 180 - image.getWidth(), Window.SIZE_Y * 0.1, 130, 130);
			}
		}

		// int textboxWidth = 600;
		// int textboxHeight = 240;
		// renderTextboxes(gc, (int) (width - textboxWidth), (int) (height -
		// textboxHeight), textboxWidth, textboxHeight,
		// souls.get(curSoul));

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
