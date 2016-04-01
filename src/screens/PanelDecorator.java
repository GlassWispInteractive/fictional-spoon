package screens;

import entities.EntityFactory;
import framework.Screen;
import framework.ScreenDecorator;
import framework.Window;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

public class PanelDecorator extends ScreenDecorator {
	private double progress;
	private int life;

	public PanelDecorator(Screen decoratedScreen) {
		super(decoratedScreen);

		addLayer("top panel", 0, 0, Window.SIZE_X, 3 * 16);
		addLayer("bottom panel", 0, Window.SIZE_Y - MapScreen.SPACE, Window.SIZE_X, MapScreen.SPACE);
		progress = 0;
	}

	public void updateProgress(double progress) {
		this.progress = progress;
		this.life = EntityFactory.getFactory().getPlayer().getHp();
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
		gc.fillRect(0, MapScreen.SPACE - 5, layers.get("top panel").getWidth(), 2);

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
			gc.strokeLine(100 + i / 100.0 * (Window.SIZE_X - 200), 5, 100 + i / 100.0 * (Window.SIZE_X - 200), 35);
		}
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

		// show life points
		gc.setFont(Window.DEFAULT_FONT);
		gc.setTextAlign(TextAlignment.LEFT);
		gc.setTextBaseline(VPos.CENTER);

		gc.fillText("life points " + life, 100, 5 + (MapScreen.SPACE - 5) / 2);
	}

}
