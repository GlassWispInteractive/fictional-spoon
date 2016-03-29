package framework;

import combat.Goal;
import combat.Objective;
import entities.EntityFactory;
import generation.Map;
import screens.AlertDecorator;
import screens.HelpScreen;
import screens.MapScreen;
import screens.PanelDecorator;

public class GameControl {
	// singleton
	private static GameControl singleton;

	private Objective objective;
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

		objective = new Objective(Goal.MONSTER, 5);

		map = new MapScreen();
		panel = new PanelDecorator(map);
		alert = new AlertDecorator(panel);
		alert.push("Walk with WASD");
		ctrl.addScreen("game", alert);

		panel.updateProgress(objective.progress());
		
		
		
//		start with intro screen
		ctrl.addScreen("intro", new HelpScreen("game", 200));
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

	public void updateGoal(Goal goal) {
		// update objective reference
		objective.add(goal);

		// check if objective is reached
		if (objective.progress() < 1) {
			panel.updateProgress(objective.progress());
		} else {
			ScreenControl.getCtrl().setScreen("game won");
		}
	}

}
