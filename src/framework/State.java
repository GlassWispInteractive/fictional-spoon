package framework;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
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
	
	private static ArrayList<double[]> souls;
	private static int soulWait;
	private Random rand = new Random();

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
		
		if(souls == null) {
		    initBackgroundSouls();
		}
		
	}

	private void initBackgroundSouls() {
		//background souls
		souls = new ArrayList<>();
		for (int i = 0; i < 25; i++) {
			int x = rand.nextInt(Window.SIZE_X - 200), y = rand.nextInt(Window.SIZE_Y - 200);
			souls.add(new double[] { 100 + x, 100 + y });
		}
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
	
	protected void computeBackgroundSouls(int ticks) {
		// soul computation
		if (soulWait > 0) {
			soulWait -= ticks;
		} else {
			soulWait = 15;

			for (double[] soul : souls) {
				soul[0] += (-1) + rand.nextInt(3);
				if (soul[0] < 50)
					soul[0] = 50;
				if (soul[0] > Window.SIZE_X - 50)
					soul[0] = Window.SIZE_X - 50;

				soul[1] += (-1) + rand.nextInt(3);
				if (soul[1] < 50)
					soul[1] = 50;
				if (soul[1] > Window.SIZE_Y - 50)
					soul[1] = Window.SIZE_Y - 50;
			}
		}
	}
 
	protected abstract void render();
	
	protected void renderBackgroundSouls(GraphicsContext gc) {
		// render background souls
		for (double[] soul : souls) {
			gc.setFill(Color.DARKRED.deriveColor(2, 1.2, 1, 0.3));
			gc.fillOval(soul[0], soul[1], 25, 25);
		}
	}
}
