package screens;

import framework.EventControl;
import framework.Screen;
import framework.ScreenControl;
import framework.Window;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class HelpScreen extends Screen {
	// class members
	private int duration, remaining;
	
	private String next;

	public HelpScreen(String next, int duration) {
		super();
		
		this.duration = duration;
		this.next = next;
		
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
			remaining -= ticks;
		} else {
			ScreenControl.getCtrl().setScreen(next);
		}
	}j

	@Override
	protected void render() {
		// start from clean screen
		GraphicsContext gc = gcs.get("main");
		gc.clearRect(0, 0, layers.get("main").getWidth(), layers.get("main").getHeight());

		// font settings
		gc.setFont(Window.HUGE_FONT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.BASELINE);
		// gc.setLineWidth(1);

		// Title
		gc.setFill(Color.RED);
		gc.setStroke(Color.RED);
		gc.fillText(Window.TITLE, Window.SIZE_X / 2, 75);
		gc.strokeLine(0, 75 + 20, Window.SIZE_X, 75 + 20);

		// single elements
		final String text[] = new String[] { "Move with WASD", "Kill 5 monsters to complete this level", "",
				"Good luck" };

		gc.setFont(Window.DEFAULT_FONT);
		for (int i = 0; i < text.length; i++) {
			gc.fillText(text[i], Window.SIZE_X / 2, 200 + i * 100);
		}
	}
}
