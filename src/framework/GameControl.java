package framework;

import entities.EntityFactory;
import generation.Map;
import screens.AlertDecorator;
import screens.MapScreen;
import screens.PanelDecorator;

public class GameControl {
	// singleton
	private static GameControl singleton;

	private MapScreen map;
	private PanelDecorator panel;
	private AlertDecorator alert;

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
		map = new MapScreen();
//		panel = new PanelDecorator(map);
		alert = new AlertDecorator(map);
		alert.push("Walk with WASD");
		ctrl.addScreen("game", alert);
	}

	/**
	 * @return the map
	 */
	public Map getMap() {
		// return map;
		return map.getMap();
	}

	public void updateCamera(int x, int y) {
		map.updateCamera(x, y);
	}

	public void alert(String string) {
		alert.push(string);
	}

}
