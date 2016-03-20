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
	private final int DUR = 10;

	// class members
	private Queue<String> alerts;
	private String alert;
	private int duration = 0;

	public AlertView(Canvas layer) {
		super(layer);
		alerts = new LinkedList<>();
	}

	@Override
	public void tick(int ticks) {
		if (duration == 0 && !alerts.isEmpty()) {
			duration = DUR;
			alert = alerts.poll();
		}

	}

	@Override
	public void render() {
		gc.setFont(Window.bigFont);

		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setLineWidth(1);

		gc.setFill(Color.DARKGRAY.deriveColor(0, 1.2, 1, 0.6));
		gc.fillText(alert, layer.getWidth() / 2, 30);
		System.out.println(layer.getWidth() / 2);
	}

	public boolean push(String alert) {
		alerts.add(alert);
		return true;
	}

}
