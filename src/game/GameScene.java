package game;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public abstract class GameScene {
	protected static GameScene singleton;

	protected Group group;
	protected Scene scene;
	protected Canvas layerBg;
	protected GraphicsContext gc;

	protected GameScene() {
		group = new Group();
		scene = new Scene(group, Window.SIZE_X, Window.SIZE_Y, Paint.valueOf("#212121"));
		layerBg = new Canvas(Window.SIZE_X, Window.SIZE_Y);
		gc = layerBg.getGraphicsContext2D();

		group.getChildren().add(layerBg);
	}

	public Scene getScene() {
		return scene;
	}

}
