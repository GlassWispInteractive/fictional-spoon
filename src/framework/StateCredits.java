package framework;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class StateCredits extends State {

	private static StateCredits singleton;

	public static StateCredits getCreditScreen() {

		if (singleton == null) {
			singleton = new StateCredits();
		}

		return singleton;
	}

	private StateCredits() {

	}

	@Override
	protected void tick(int ticks) {
		SpookingSouls.getObject().tick(ticks);
	}

	@Override
	protected void render() {

		// start from clean screen
		GraphicsContext gc = gcs.get("main");
		gc.clearRect(0, 0, Window.SIZE_X, Window.SIZE_Y);

		SpookingSouls.getObject().render(gc);

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

	}

}
