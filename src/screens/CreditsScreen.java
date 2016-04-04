package screens;

import framework.Consts;
import framework.Screen;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.TextAlignment;

public class CreditsScreen extends Screen {

	private static CreditsScreen singleton;

	public static CreditsScreen getScreen() {

		if (singleton == null) {
			singleton = new CreditsScreen();
		}

		return singleton;
	}

	private CreditsScreen() {

	}

	@Override
	protected void tick(int ticks) {
		// SpookingSouls.getObject().tick(ticks);
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

		// Title
		gc.setFill(Consts.RED);
		gc.setStroke(Consts.RED);
		gc.fillText(Consts.TITLE, Consts.SIZE_X / 2, 75);
		gc.strokeLine(0, 75 + 20, Consts.SIZE_X, 75 + 20);

		// single elements
		final String credits[] = new String[] { "coding by dhaunac", "coding by garax91", "tile sets by kenney.nl",
				"element pictures from Korra" };

		gc.setFont(Consts.DEFAULT_FONT);
		for (int i = 0; i < credits.length; i++) {
			gc.fillText(credits[i], Consts.SIZE_X / 2, 200 + i * 100);
		}

	}

}
