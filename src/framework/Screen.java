package framework;

import java.util.HashMap;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

public abstract class Screen {
	// singleton object
	protected static Screen singleton;

	// class members
	protected Group group;
	protected Scene scene;
	protected HashMap<String, Canvas> layers;
	protected HashMap<String, GraphicsContext> gcs;

	protected Screen() {
		// init group and scene as root of this scene
		group = new Group();
		scene = new Scene(group, Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT, Paint.valueOf("#212121"));

		// create layers and extract their gc
		layers = new HashMap<>();
		layers.put("main", new Canvas(Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT));

		gcs = new HashMap<>();
		gcs.put("main", layers.get("main").getGraphicsContext2D());

		group.getChildren().add(layers.get("main"));
	}

	/**
	 * add a new layer to the root
	 * 
	 * @param layer
	 */
	protected void addLayer(String name, double x, double y, double w, double h) {
		Canvas layer = new Canvas(w, h);
		group.getChildren().add(layer);
		layer.relocate(x, y);

		// update hash maps
		layers.put(name, layer);
		gcs.put(name, layer.getGraphicsContext2D());
	}

	/**
	 * add a new layer to some group
	 * 
	 * @param layer
	 */
	protected void stackLayer(Pane pane, String name, double x, double y, double w, double h) {
		Canvas layer = new Canvas(w, h);
		pane.getChildren().add(layer);
		layer.relocate(x, y);

		// update hash maps
		layers.put(name, layer);
		gcs.put(name, layer.getGraphicsContext2D());
	}

	public Scene getScene() {
		return scene;
	}

	protected abstract void tick(int ticks);

	protected abstract void render();

}
