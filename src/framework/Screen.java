package framework;

import java.util.HashMap;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;

public abstract class Screen {
	// singleton object
	protected static Screen singleton;

	// internal organisation
	private ScreenControl ctrl;
	private Screen parent = null;

	// class members
	protected Group group;
	protected Scene scene;
	protected HashMap<String, Canvas> layers;
	protected HashMap<String, GraphicsContext> gcs;
	protected boolean paused;

	protected Screen() {
		// get control object
		ctrl = ScreenControl.getCtrl();

		// init group and scene as root of this scene
		group = new Group();
		scene = new Scene(group, Window.SIZE_X, Window.SIZE_Y, Paint.valueOf("#212121"));

		// create layers and extract their gc
		layers = new HashMap<>();
		layers.put("main", new Canvas(Window.SIZE_X, Window.SIZE_Y));

		gcs = new HashMap<>();
		gcs.put("main", layers.get("main").getGraphicsContext2D());

		group.getChildren().add(layers.get("main"));

		// pause state is false
		paused = false;

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

	/**
	 * start this scene
	 */
	public void start() {
		parent = ctrl.getState();

		active();
	}

	/**
	 * stop this scene and return to parental scene
	 */
	public void stop() {
		if (parent != null) {
			parent.active();
		}
	}

	/**
	 * go back every state until the root state is reached
	 */
	public void clean() {
		Screen aux = this;
		while (aux.parent != null) {
			aux = aux.parent;
		}
		aux.active();
	}

	/**
	 * internal function to put this state active
	 */
	private void active() {

		ctrl.setState(this);

		Window.setScene(scene);
	}

	protected abstract void tick(int ticks);

	protected abstract void render();

}
