package framework;

import java.util.HashMap;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public abstract class State {
	// singleton object
	protected static State singleton;

	// internal organisation
	private StateControl ctrl;
	private State parent = null;

	// class members
	protected Group group;
	protected Scene scene;
	protected HashMap<String, Canvas> layers;
	protected HashMap<String, GraphicsContext> gcs;
	protected boolean paused;
	

	protected State() {
		// get control object
		ctrl = StateControl.getCtrl();

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
	 * add a new layer to this scene
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
		State aux = this;
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
