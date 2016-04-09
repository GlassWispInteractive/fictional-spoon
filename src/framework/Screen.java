package framework;

import java.util.HashMap;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

public abstract class Screen {
	// singleton object
	protected static Screen singleton;

	// class members
	protected Group group;
	protected Scene scene;
	protected HashMap<String, Node> nodes;
	protected HashMap<String, GraphicsContext> gcs;

	protected Screen() {
		// init group and scene as root of this scene
		group = new Group();
		scene = new Scene(group, Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT, Global.DARKGRAY);

		// create layers and extract their gc
		Canvas canvas = new Canvas(Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT);
		nodes = new HashMap<>();
		nodes.put("main", canvas);

		gcs = new HashMap<>();
		gcs.put("main", canvas.getGraphicsContext2D());

		group.getChildren().add(nodes.get("main"));
	}

	/**
	 * add a new layer to the root
	 * 
	 * @param layer
	 */
	protected void addCanvas(String name, double x, double y, double w, double h) {
		Canvas layer = new Canvas(w, h);
		group.getChildren().add(layer);
		layer.relocate(x, y);

		// update hash maps
		nodes.put(name, layer);
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
		nodes.put(name, layer);
		gcs.put(name, layer.getGraphicsContext2D());
	}

	public Scene getScene() {
		return scene;
	}

	protected abstract void tick(int ticks);

	protected abstract void render();

}
