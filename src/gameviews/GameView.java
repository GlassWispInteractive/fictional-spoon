package gameviews;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public abstract class GameView {
	protected Canvas layer;
	protected GraphicsContext gc;

	public GameView(Canvas layer) {
		this.layer = layer;
		this.gc = layer.getGraphicsContext2D();
	}

	public abstract void tick(int ticks);

	public abstract void render();
}
