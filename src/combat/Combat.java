package combat;

import java.util.ArrayList;

import entities.EntityFactory;
import entities.Monster;
import game.Events;
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
	}

	private static ArrayList<Monster> getHardCodedMonster() {
		ArrayList<Monster> list = new ArrayList<Monster>();

		list.add(EntityFactory.getFactory().makeMonster(13, 13, 0, new int[] { 1, 2, 3, 4, 5 }, "name"));
		list.add(EntityFactory.getFactory().makeMonster(12, 23, 0, new int[] { 1, 2, 3, 4, 5 }, "test"));
		list.add(EntityFactory.getFactory().makeMonster(14, 33, 0, new int[] { 1, 2, 3, 4, 5 }, "rudolf"));

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
		// System.out.println(ticks);
		status = Math.min(1, status + ticks / 120);

		Events e = Events.getEvents();

		// if (e.isLeft()) {
		// curSoul = (curSoul + souls.size() - 1) % souls.size();
		// }
		// if (e.isRight()) {
		// curSoul = (curSoul + 1) % souls.size();
		// }
		//

		// number pressed
		if (e.isOne())
			curSoul = 0;
		if (e.isTwo())
			curSoul = 1;
		if (e.isThree())
			curSoul = 2;
		if (e.isFour())
			curSoul = 3;

		// arrow pressed
		if (e.isLeft()) {
			curFocus = (curFocus + 1) % monster.size();
		}
		if (e.isRight()) {
			curFocus = (curFocus + monster.size() - 1) % monster.size();
		}

		if (e.isEnter() || status == 1) {

			if (status > lowerBound && status < upperBound) {
				System.out.println("Bonus damage");
			}

			// reset bar progress
			status = 0;
		}

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

	public void render(GraphicsContext gc) {
		// font settings
		Font font = Font.font("Helvetica", FontWeight.NORMAL, 16);
		gc.setFont(font);

		gc.setTextAlign(TextAlignment.LEFT);
		gc.setTextBaseline(VPos.BASELINE);

		// // background
		// gc.setFill(Paint.valueOf("#C0C0C0"));
		// gc.fillRect(0, 0, width, height);

		// fancy line at 40%
		gc.setLineWidth(3);
		// gc.setFill(Color.ANTIQUEWHITE);
		// gc.fillRoundRect(100, height * 0.4, Window.SIZE_X - 200, 30, 30, 30);
		gc.setStroke(Color.GREY);
		gc.strokeRoundRect(100, Window.SIZE_Y * 0.4, Window.SIZE_X - 200, 30, 30, 30);

		// status = 0.5;
		gc.setFill(Color.ORANGE);
		gc.fillRoundRect(110, Window.SIZE_Y * 0.4 + 5, status * (Window.SIZE_X - 220), 20, 20, 20);

		gc.setStroke(Color.GREY);

		// draw two border delimter for inner box
		// gc.strokePolyline(new double[] { 110, 110 }, new double[] {
		// Window.SIZE_Y * 0.4, Window.SIZE_Y * 0.4 + 30 }, 2);
		// gc.strokePolyline(new double[] { Window.SIZE_X - 110, Window.SIZE_X -
		// 110 },
		// new double[] { Window.SIZE_Y * 0.4, Window.SIZE_Y * 0.4 + 30 }, 2);

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

			if (curSoul == i) {
				gc.setStroke(Color.ANTIQUEWHITE);
				gc.setLineWidth(3);
				gc.strokeRect(50 + i * 180, Window.SIZE_Y * 0.65, ELEMS[i].getWidth() / 3, ELEMS[i].getHeight() / 3);
			}
		}

		// monster at 10% height
		for (int i = 0; i < monster.size(); i++) {
			gc.setFill(Color.RED);
			gc.fillText(monster.get(i).getName(), Window.SIZE_X - 150 - i * 120, Window.SIZE_Y * 0.1 - 5, 80);
			gc.fillRect(Window.SIZE_X - 150 - i * 120, Window.SIZE_Y * 0.1, 80, 80);

			if (curFocus % monster.size() == i) {
				gc.setStroke(Color.BLACK);
				gc.setLineWidth(4);
				gc.strokeRect(Window.SIZE_X - 150 - i * 120, Window.SIZE_Y * 0.1, 80, 80);
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
