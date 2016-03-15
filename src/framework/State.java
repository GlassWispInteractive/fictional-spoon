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

	// stuff
//	protected static StateName name;
//	protected static ArrayList<StateName, State> states = new HashMap<>();

	// class members
	protected Group group;
	protected Scene scene;
	protected ArrayList<Canvas> layers;
	protected ArrayList<GraphicsContext> gcs;
	protected boolean paused;

	protected State() {
//		states.put(name, this);

		group = new Group();
		scene = new Scene(group, Window.SIZE_X, Window.SIZE_Y, Paint.valueOf("#212121"));

		layers = new ArrayList<>();
		layers.add(new Canvas(Window.SIZE_X, Window.SIZE_Y));

		gcs = new ArrayList<>();
		gcs.add(layers.get(0).getGraphicsContext2D());

		group.getChildren().add(layers.get(0));
		
		paused = false;
	}

	protected void addLayer(Canvas layer) {
		layers.add(layer);
		gcs.add(layer.getGraphicsContext2D());
		group.getChildren().add(layer);
	}
	
	protected void start() {
		StateControl ctrl = StateControl.getCtrl();
		ctrl.setState(this);
		
		Window.setScene(scene);
	}
}
