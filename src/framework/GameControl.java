package framework;

import gameviews.AlertView;
import gameviews.GameView;
import gameviews.InfoView;
import gameviews.MapView;
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
	private final int n = Views.values().length;

	private MapView mapView;
	private InfoView infoView;

	private AlertView alertView;

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

		views = new GameView[n];

		addLayer("map", 0, 0, 350 * size, 225 * size);
		addLayer("entities", 0, 0, Window.SIZE_X, Window.SIZE_Y);
		mapView = new MapView(layers.get("map"), layers.get("entities"));
		views[Views.MAP.ordinal()] = mapView;

		addLayer("info", 0, Window.SIZE_Y - 50, Window.SIZE_X, 50);
		infoView = new InfoView(layers.get("info"));
		views[Views.INFO.ordinal()] = infoView;

		addLayer("alert", 0, 300, Window.SIZE_X, 100);
		alertView = new AlertView(layers.get("alert"));
		alertView.push("Walk with WASD");
		views[Views.ALERT.ordinal()] = alertView;

	}

	/**
	 * @return the map
	 */
	public Map getMap() {
		// return map;
		return mapView.getMap();
	}

	/**
	 * method is called every tick
	 * 
	 * @param ticks
	 */
	@Override
	public void tick(int ticks) {
		for (int i = 0; i < n; i++) {
			if (views[i] == null)
				continue;

			views[i].tick(ticks);
		}
	}

	@Override
	protected void render() {
		for (int i = 0; i < n; i++) {
			if (views[i] == null)
				continue;

			views[i].render();
		}
	}

	public void updateCamera(int x, int y) {
		mapView.updateCamera(x, y);
	}

	public void alert(String string) {
		alertView.push(string);
	}

}
