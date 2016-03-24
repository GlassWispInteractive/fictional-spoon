package screens;

import framework.Screen;
import framework.ScreenDecorator;
import framework.Window;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class PanelDecorator extends ScreenDecorator {

	public PanelDecorator(Screen decoratedScreen) {
		super(decoratedScreen);

		addLayer("top panel", 0, 0, Window.SIZE_X, MapScreen.MARGIN);
		addLayer("bottom panel", 0, Window.SIZE_Y - MapScreen.MARGIN, Window.SIZE_X, MapScreen.MARGIN);
	}

	@Override
	public void tick(int ticks) {
		// call decorated screen
		super.tick(ticks);

	}

	@Override
	public void render() {
		// call decorated screen
		super.render();

		renderTopPanel();
		renderBottomPanel();
	}

	private void renderTopPanel() {
		// clear screen
		GraphicsContext gc = gcs.get("top panel");
		gc.clearRect(0, 0, layers.get("top panel").getWidth(), layers.get("top panel").getHeight());

		// draw background
		gc.setFill(Paint.valueOf("#212121"));
		gc.fillRect(0, 0, layers.get("top panel").getWidth(), layers.get("top panel").getHeight());
		gc.setFill(Color.ANTIQUEWHITE);
		gc.fillRect(0, MapScreen.MARGIN - 7, layers.get("top panel").getWidth(), 2);

		
		final double progress = 0.66;
		
		// draw objective progress
		gc.setLineWidth(3);
		gc.setStroke(Color.GREY);
		gc.strokeRect(100, 5, Window.SIZE_X - 200, 30);

		// progress bar
		gc.setFill(Color.DARKGREEN.deriveColor(1, progress, 2, 1));
		gc.fillRect(100, 10, progress * (Window.SIZE_X - 200), 20);

		// draw the delimters each 5 percents
		// use ints, because of double problems with exact numbers
		gc.setLineWidth(2);
		gc.setStroke(Color.GREY);
		for (int i = 5; i < 100; i += 5) {
			gc.strokePolyline(new double[] { 110 + i/100.0 * (Window.SIZE_X - 220), 110 + i/100.0 * (Window.SIZE_X - 220) },
					new double[] { 5, 35 }, 2);
		}

		//
		// gc.strokePolyline(
		// new double[] { 110 + upperBound * (Window.SIZE_X - 220), 110 +
		// upperBound * (Window.SIZE_X - 220) },
		// new double[] { 5, 35 }, 2);
	}

	private void renderBottomPanel() {
		// clear screen
		GraphicsContext gc = gcs.get("bottom panel");
		gc.clearRect(0, 0, layers.get("bottom panel").getWidth(), layers.get("bottom panel").getHeight());

		// draw background
		gc.setFill(Paint.valueOf("#212121"));
		gc.fillRect(0, 0, layers.get("bottom panel").getWidth(), layers.get("bottom panel").getHeight());
		gc.setFill(Color.ANTIQUEWHITE);
		gc.fillRect(0, 5, layers.get("bottom panel").getWidth(), 2);
	}

}