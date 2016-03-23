package screens;

import java.util.ArrayList;

import com.sun.javafx.tk.Toolkit;

import combat.Combo;
import framework.Window;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class ComboScreen extends GameView {

	public ComboScreen(Canvas layer) {
		super(layer);
	}

	@Override
	public void tick(int ticks) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		// start from clean screen
		gc.clearRect(0, 0, Window.SIZE_X, Window.SIZE_Y);
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, Window.SIZE_X, Window.SIZE_Y);

		// font settings
		gc.setFont(Window.NORMAL_FONT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.BASELINE);
		// gc.setLineWidth(1);

		String[][] comboNames = splitComboNames();

		int textHeight = (int) Toolkit.getToolkit().getFontLoader().getFontMetrics(gc.getFont()).getLineHeight();
		int padding = 10;
		int width = Window.SIZE_X / Math.max(1, comboNames.length);
		int height = (int) (1.5 * textHeight);
		int startY = 50;
		int rowY;
		int columnX = 0;

		// draw title
		gc.setFill(Color.RED);
		gc.setStroke(Color.RED);
		gc.setFont(Window.BIG_FONT);
		if (comboNames.length == 0) {
			gc.fillText("No Combos", Window.SIZE_X / 2, startY);
		} else {
			gc.fillText("Combos:", Window.SIZE_X / 2, 50);
		}
		gc.strokeLine(0, startY + padding, Window.SIZE_X, startY + padding);

		// draw all combos in use
		for (int i = 0; i < comboNames.length; i++) {

			rowY = startY + height + padding;

			for (int j = 0; j < comboNames[i].length; j++) {

				gc.setFill(Color.DARKRED);
				if (comboNames[i][j] != null) {
					gc.fillText(comboNames[i][j].toString(), columnX + width / 2, rowY, width - 2 * padding);
				}

				rowY += height;
			}

			columnX += width;
		}
	}

	private String[][] splitComboNames() {

		String[][] array;

		ArrayList<Combo> combosInUse = new ArrayList<Combo>(Combo.getCombosInUse());

		float rowNumber = 0f;
		int columnNumber;

		if (combosInUse.size() == 0) {
			rowNumber = 0;
		} else if (combosInUse.size() <= 15) {
			rowNumber = 1;
		} else if (combosInUse.size() <= 30) {
			rowNumber = 2;
		} else if (combosInUse.size() <= 45) {
			rowNumber = 3;
		} else if (combosInUse.size() <= 60) {
			rowNumber = 4;
		} else {
			rowNumber = 5;
		}

		columnNumber = (int) Math.ceil(combosInUse.size() / rowNumber);

		array = new String[(int) rowNumber][columnNumber];

		for (int i = 0; i < rowNumber; i++) {
			for (int j = 0; j < columnNumber; j++) {

				if (combosInUse.size() > 0) {
					array[i][j] = combosInUse.get(combosInUse.size() - 1).toString();
					combosInUse.remove(combosInUse.size() - 1);
				} else {
					break;
				}
			}
		}

		return array;
	}

}
