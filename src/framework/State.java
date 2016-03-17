package framework;

import java.util.ArrayList;
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
	protected ArrayList<Canvas> layers;
	protected ArrayList<GraphicsContext> gcs;
	protected boolean paused;

	protected State() {
		// get control object
		ctrl = StateControl.getCtrl();

		// init group and scene as root of this scene
		group = new Group();
		scene = new Scene(group, Window.SIZE_X, Window.SIZE_Y, Paint.valueOf("#212121"));

		// create layers and extract their gc
		layers = new ArrayList<>();
		layers.add(new Canvas(Window.SIZE_X, Window.SIZE_Y));

		gcs = new ArrayList<>();
		gcs.add(layers.get(0).getGraphicsContext2D());

		group.getChildren().add(layers.get(0));

		// pause state is false
		paused = false;
	}

	/**
	 * add a new layer to this scene
	 * 
	 * @param layer
	 */
	protected void addLayer(Canvas layer) {
		layers.add(layer);
		gcs.add(layer.getGraphicsContext2D());
		group.getChildren().add(layer);
	}

	/**
	 * start this scene
	 */
	public void start() {
		parent = ctrl.getState();

		changeState();
	}
	
	/**
	 * stop this scene and return to parental scene
	 */
	public void stop() {
		
		if(parent != null){
			parent.changeState();
		}
	}
	
	private void changeState(){
		
		ctrl.setState(this);

		Window.setScene(scene);
	}

	protected abstract void tick(int ticks);

	protected abstract void render();
}
