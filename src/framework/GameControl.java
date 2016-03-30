package framework;

import combat.Goal;
import combat.Objective;
import entities.EntityFactory;
import generation.LevelBuilder;
import generation.Map;
import screens.AlertDecorator;
import screens.HelpScreen;
import screens.MapScreen;
import screens.PanelDecorator;

public class GameControl {
	// singleton
	private static GameControl singleton;

	private HelpScreen screen;

	private Objective objective;
	private MapScreen map;
	private PanelDecorator panel;
	private AlertDecorator alert;
	private int level;

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

		// settings
		screen = new HelpScreen("game", 200);
		ScreenControl.getCtrl().addScreen("game intro", screen);
		level = 1;
		loadObjective(level);

	}

	private void loadObjective(int level) {
		//
		ScreenControl ctrl = ScreenControl.getCtrl();

		if (level > 1) {
			// reset stuff
			EntityFactory.resetGame();
		}

		// set the appropriate objective
		switch (level) {
		case 1:
			screen.setText(new String[] { "Welcome", "Quest: Kill 2 monsters" });
			objective = new Objective(Goal.MONSTER, 2);

			map = new MapScreen(LevelBuilder.newRandomLevel(350, 225));
			break;
		case 2:
			objective = new Objective(Goal.MONSTER, 5);
			screen.setText(new String[] { "Quest completed", "Quest: Kill 5 monsters" });
			ctrl.setScreen("game intro");

			map = new MapScreen(LevelBuilder.newRandomLevel(350, 225));
			break;
		case 3:
			objective = new Objective(Goal.MONSTER, 5);
			screen.setText(new String[] { "Quest completed", "Quest: Kill 5 monsters" });
			ctrl.setScreen("game intro");

			map = new MapScreen(LevelBuilder.newRandomLevel(350, 225));
			break;
		case 4:
			objective = new Objective(Goal.MONSTER, 5);
			screen.setText(new String[] { "Quest completed", "Quest: Kill 5 monsters" });
			ctrl.setScreen("game intro");

			map = new MapScreen(LevelBuilder.newRandomLevel(350, 225));
			break;
		case 5:
			objective = new Objective(Goal.MONSTER, 5);
			screen.setText(new String[] { "Quest completed", "Quest: Kill 5 monsters" });
			ctrl.setScreen("game intro");

			map = new MapScreen(LevelBuilder.newRandomLevel(350, 225));
			break;
		default:

		}

		// update game screens
		panel = new PanelDecorator(map);
		alert = new AlertDecorator(panel);
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

	public void updateGoal(Goal goal) {
		// update objective reference
		objective.add(goal);

		if (objective.progress() < 1) {
			// update panel, game is going
			panel.updateProgress(objective.progress());
		} else {
			// objective is reached
			if (level == 5) {
				ScreenControl.getCtrl().setScreen("game won");
			} else {
				level++;
				loadObjective(level);
			}
		}
	}

}
