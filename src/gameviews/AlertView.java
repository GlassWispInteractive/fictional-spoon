package gameviews;

import java.util.LinkedList;
import java.util.Queue;

import framework.Window;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class AlertView extends GameView {
	// constants

	private final double REPETITIONS = 2, SPEED = 25, DURATION = Math.PI * SPEED * REPETITIONS;

	// class members
	private Queue<String> alerts;
	private String alert;
	private int remainingTicks = 0;
	private double alpha;

	public AlertView(Canvas layer) {
		// inits
		super(layer);
		alerts = new LinkedList<>();

		// font settings to be set only once
		gc.setFont(Window.BIG_FONT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setLineWidth(1);
	}

	@Override
	public void tick(int ticks) {
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
		gc.clearRect(0, 0, layer.getWidth(), layer.getHeight());

		// dont render if the alert should not be viewed
		if (remainingTicks <= 0) {
			return;
		}

		// render text with alpha opcity
		gc.setFill(Color.RED.deriveColor(0, 1.2, 1, alpha));
		gc.fillText(alert, layer.getWidth() / 2, 50);
	}

	public void push(String alert) {
		alerts.add(alert);
	}

}
