package screens;

import framework.EventControl;
import framework.Consts;
import framework.Screen;
import framework.ScreenControl;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.TextAlignment;

public class HelpScreen extends Screen {
	// class members
	private int duration, remaining;
	private String nextScreen;
	private String text[];
	private int index;

	public HelpScreen(String next, int duration) {
		super();

		text = new String[] { "" };
		this.duration = duration;
		this.nextScreen = next;

		remaining = this.duration;
	}

	@Override
	protected void tick(int ticks) {
		// on enter skip the screen
		if (EventControl.getEvents().isEnter()) {
			remaining = 0;
			EventControl.getEvents().clear();
		}

		if (remaining > 0) {
			index = (int) Math.floor((double) text.length * (duration - remaining) / duration);

			remaining -= ticks;
		} else {
			ScreenControl.getCtrl().setScreen(nextScreen);
			remaining = duration;
		}
	}

	@Override
	protected void render() {
		// start from clean screen
		GraphicsContext gc = gcs.get("main");
		gc.clearRect(0, 0, layers.get("main").getWidth(), layers.get("main").getHeight());

		// font settings
		gc.setFont(Consts.HUGE_FONT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.BASELINE);
		// gc.setLineWidth(1);

		// text
		gc.setFill(Consts.RED);
		gc.setStroke(Consts.RED);

		gc.fillText(text[index], Consts.SIZE_X / 2, Consts.SIZE_Y * 0.4);
		// gc.strokeLine(0, 75 + 20, Window.SIZE_X, 75 + 20);

		// gc.setFont(Window.DEFAULT_FONT);
		// for (int i = 0; i < text.length; i++) {
		// gc.fillText(text[i], Window.SIZE_X / 2, 200 + i * 100);
		// }
	}

	public void setText(String[] text) {
		this.text = text;
	}
}
