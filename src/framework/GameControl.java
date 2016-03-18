package framework;

import gameviews.AltertView;
import gameviews.GameView;
import generation.Map;

public class GameControl extends State {
	// map settings
	private final int size = 16;

	// singleton
	private static GameControl singleton;

	// class components
	protected enum Views {
		ALERT, COMBAT, COMBO, INFO, MAP, OBJECTIVE
	};

	private GameView[] views;

	/**
	 * static method to get the singleton class object
	 * 
	 * @return
	 */
	public static GameControl getControl() {
		if (singleton == null) {
			singleton = new GameControl();
		}
		return singleton;
	}

	private GameControl() {
		// call the very important state constructor
		super();

		// views count
		final int n = Views.values().length;

		views = new GameView[n];
		views[Views.ALERT.ordinal()] = new AltertView(null);

		// add layer
		addLayer("map", 0, 0, 1 * size, 1 * size);
		// image
		addLayer("entities", 0, 0, Window.SIZE_X, Window.SIZE_Y);
	}

	/**
	 * @return the map
	 */
	public Map getMap() {
		// return map;
		return null;
	}

	/**
	 * method is called every tick
	 * 
	 * @param ticks
	 */
	@Override
	public void tick(int ticks) {

	}

	@Override
	protected void render() {

	}

	public void updateCamera(int x, int y) {
		// views[MAP].updateCamera(x, y);
		// wrong for now

	}

}
