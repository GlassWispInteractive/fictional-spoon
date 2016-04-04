package screens;

import entities.EntityFactory;
import framework.Consts;
import framework.Screen;
import framework.ScreenDecorator;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class PanelDecorator extends ScreenDecorator {
	private double progress;
	private int life;

	public PanelDecorator(Screen decoratedScreen) {
		super(decoratedScreen);

		addLayer("top panel", 0, 0, Consts.SIZE_X, 3 * 16);
		addLayer("bottom panel", 0, Consts.SIZE_Y - Consts.PANEL_SIZE, Consts.SIZE_X, Consts.PANEL_SIZE);
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
		gc.setFill(Consts.DARKGRAY);
		gc.fillRect(0, 0, layers.get("top panel").getWidth(), layers.get("top panel").getHeight());
		gc.setFill(Consts.WHITE);
		gc.fillRect(0, Consts.PANEL_SIZE - 5, layers.get("top panel").getWidth(), 2);

		// print out label
		gc.setFont(Consts.DEFAULT_FONT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.fillText("quest progress", 125, -5 + (Consts.PANEL_SIZE - 5) / 2);

		// draw objective progress
		gc.setLineWidth(3);
		gc.setStroke(Consts.WHITE);
		gc.strokeRect(250, 5, Consts.SIZE_X - 300, 30);

		// progress bar
		gc.setFill(Color.DARKGREEN.deriveColor(1, progress, 2, 1));
		gc.fillRect(250, 10, progress * (Consts.SIZE_X - 300), 20);

		// draw the delimters each 5 percents
		// use ints, because of double problems with exact numbers
		gc.setLineWidth(2);
		gc.setStroke(Consts.WHITE);
		for (int i = 5; i < 100; i += 5) {
			gc.strokeLine(250 + i / 100.0 * (Consts.SIZE_X - 300), 5, 250 + i / 100.0 * (Consts.SIZE_X - 300), 35);
		}
	}

	private void renderBottomPanel() {
		// clear screen
		GraphicsContext gc = gcs.get("bottom panel");
		gc.clearRect(0, 0, layers.get("bottom panel").getWidth(), layers.get("bottom panel").getHeight());

		// draw background
		gc.setFill(Consts.DARKGRAY);
		gc.fillRect(0, 0, layers.get("bottom panel").getWidth(), layers.get("bottom panel").getHeight());
		gc.setFill(Consts.WHITE);
		gc.fillRect(0, 5, layers.get("bottom panel").getWidth(), 2);

		// font settings
		gc.setFont(Consts.DEFAULT_FONT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);

		// print out text boxes
		gc.fillText("life points " + life, Consts.WIDTH * .25, 5 + (Consts.PANEL_SIZE - 5) / 2);
		gc.fillText("view combos (C)", Consts.WIDTH * .5, 5 + (Consts.PANEL_SIZE - 5) / 2);
		gc.fillText("view quest (Q)", Consts.WIDTH * .75, 5 + (Consts.PANEL_SIZE - 5) / 2);

	}

}
