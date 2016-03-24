package screens;

import java.util.ArrayList;
import java.util.Arrays;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;

import framework.EventControl;
import framework.GameControl;
import framework.Screen;
import framework.ScreenControl;
import framework.Window;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class FinishScreen extends Screen {
	private final String won = "You have won the game", lost = "Game over";
	private boolean finish = false;

	private int cur;
	private ArrayList<String> list = new ArrayList<String>(Arrays.asList(new String[] { "Restart", "Credit", "Exit" }));

	public FinishScreen(boolean finish) {
		this.finish = finish;
	}

	@Override
	protected void tick(int ticks) {
		EventControl e = EventControl.getEvents();

		// SpookingSouls.getObject().tick(ticks);
		// event handling
		if (e.isLeft())
			cur = (cur + list.size() - 1) % list.size();

		if (e.isRight())
			cur = (cur + 1) % list.size();

		if (e.isEnter()) {
			// TODO: put resetgame properly
			GameControl.resetGame();

			switch (list.get(cur)) {
			case "Restart":
				ScreenControl.getCtrl().setScreen("menu");
				break;
			case "Credit":
				ScreenControl.getCtrl().setScreen("credits");
				break;
			case "Exit":
				System.exit(0);
				break;
			default:
				break;
			}
		}

		// no double key activation
		EventControl.getEvents().clear();
	}

	@Override
	protected void render() {

		// start from clean screen
		GraphicsContext gc = gcs.get("main");
		gc.clearRect(0, 0, Window.SIZE_X, Window.SIZE_Y);

		// font settings
		gc.setFont(Window.HUGE_FONT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.BASELINE);
		// gc.setLineWidth(1);

		// SpookingSouls.getObject().render(gc);

		// Game Over
		gc.setFill(Color.RED);
		gc.fillText(finish ? won : lost, Window.SIZE_X / 2, Window.SIZE_Y / 2);

		// List
		// calc textLength

		gc.setFont(Window.DEFAULT_FONT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setLineWidth(1);

		FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
		int textWidth = (int) fontLoader.computeStringWidth("", gc.getFont());
		int textHeight = (int) fontLoader.getFontMetrics(gc.getFont()).getLineHeight();

		for (int i = 0; i < list.size(); i++) {
			if (textWidth < (int) fontLoader.computeStringWidth(list.get(i).toString(), gc.getFont())) {
				textWidth = (int) fontLoader.computeStringWidth(list.get(i).toString(), gc.getFont());
			}
		}

		int padding = 10;
		int width = textWidth + 6 * padding;
		int height = (int) (1.5 * textHeight);
		int rowY = (int) (Window.SIZE_Y * 0.8);
		int columnX = (int) (Window.SIZE_X / 2 - list.size() * width / 2);

		for (int j = 0; j < Math.min(10, list.size()); j++) {

			gc.setFill(Color.DARKGRAY.deriveColor(0, 1.2, 1, 0.6));
			gc.setStroke(Color.DARKGRAY);
			gc.fillRoundRect(columnX + padding, rowY, width - padding, height, 60, 200);

			if (j != cur) {
				gc.setFill(Color.DARKRED.deriveColor(0, 1.2, 1, 0.6));
				gc.setStroke(Color.DARKRED);
			} else {
				gc.setFill(Color.RED.deriveColor(0, 1.2, 1, 0.6));
				gc.setStroke(Color.RED);
			}

			gc.fillText(list.get(j).toString(), columnX + width / 2, rowY + height / 2);
			gc.strokeText(list.get(j).toString(), columnX + width / 2, rowY + height / 2);

			columnX += width;
		}
	}
}
