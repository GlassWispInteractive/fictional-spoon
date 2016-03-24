package framework;

import java.util.Set;
import entities.EntityFactory;
import generation.Map;
import screens.AlertDecorator;
import screens.GameView;
import screens.MapScreen;

public class GameControl extends Screen {
	// singleton
	private static GameControl singleton;

	// class components
	protected enum Views {
		COMBAT, COMBO, MAP
	};

	private Set<GameView> collection;

	private MapScreen mapView;
	private AlertDecorator alertView;

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

	public static void resetGame() {
		// reset EntityFactory
		EntityFactory.resetGame();

		singleton = new GameControl();
	}

	private GameControl() {
		// call the very important state constructor
		super();

		ScreenControl ctrl = ScreenControl.getCtrl();
		mapView = new MapScreen();
		ctrl.addScreen("game", mapView);
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
		for (GameView view : collection) {
			view.tick(ticks);
		}
	}

	@Override
	protected void render() {
		for (GameView view : collection) {
			view.render();
		}
	}

	public void updateCamera(int x, int y) {
		mapView.updateCamera(x, y);
	}

	public void alert(String string) {
		// alertView.push(string);
	}

}
