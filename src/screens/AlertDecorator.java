package screens;

import java.util.LinkedList;
import java.util.Queue;

import framework.Global;
import framework.Screen;
import framework.ScreenDecorator;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class AlertDecorator extends ScreenDecorator {
	// constants

	private final double REPETITIONS = 1, SPEED = 20, DURATION = Math.PI * SPEED * REPETITIONS;

	// class members
	private Queue<String> alerts;
	private String alert;
	private int remainingTicks = 0;
	private double alpha;

	public AlertDecorator(Screen decoratedScreen) {
		// inits
		super(decoratedScreen);

		addCanvas("alert", 0, Global.WINDOW_HEIGHT / 2 - 50, Global.WINDOW_WIDTH, 100);
		alerts = new LinkedList<>();
	}

	@Override
	public void tick(int ticks) {
		super.tick(ticks);

		if (remainingTicks <= 0 && !alerts.isEmpty()) {
			remainingTicks = (int) DURATION;
			alert = alerts.poll();
		}

		// might get negative duration
		if (remainingTicks > 0) {
			remainingTicks -= ticks;
			alpha = 0.3 + 0.7 * Math.abs(Math.sin((DURATION - remainingTicks) / SPEED));
		}
	}

	@Override
	public void render() {
		super.render();

		// clear screen
		GraphicsContext gc = gcs.get("alert");
		gc.clearRect(0, 0, gcs.get("alert").getCanvas().getWidth(), gcs.get("alert").getCanvas().getHeight());

		// dont render if the alert should not be viewed
		if (remainingTicks <= 0) {
			return;
		}

		// font settings to be set
		gc.setFont(Global.BIG_FONT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setLineWidth(1);

		// render text with alpha opcity
		gc.setFill(Color.RED.deriveColor(0, 1.2, 1, alpha));
		gc.fillText(alert, gcs.get("alert").getCanvas().getWidth() / 2, 50);
	}

	public void push(String alert) {
		alerts.add(alert);
	}

}
