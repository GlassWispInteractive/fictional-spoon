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
		screen = new HelpScreen("game", 180);
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

		// set the appropriate objective by level
		switch (level) {
		case 1:
			// basic level
			screen.setText(new String[] { "Quest: Search the shrine" });
			map = new MapScreen(
					new LevelBuilder(Window.SIZE_X / 16, Window.SIZE_Y / 16 - 6, LevelBuilder.Layout.SINGLE_CONN_ROOMS)
							.genMonster(1, 0.01).genShrine(0.1, 0).create());
			objective = new Objective(Goal.SHRINE, 1);
			break;
		case 2:
			// default level
			screen.setText(new String[] { "Quest completed", "Quest: Kill 30 Monster" });
			ctrl.setScreen("game intro");

			map = new MapScreen(new LevelBuilder(300, 200, LevelBuilder.Layout.LOOPED_ROOMS).genMonster(1.3, 0.1)
					.genChest(0.2, 0.01).create());
			objective = new Objective(Goal.MONSTER, 30);
			break;
		case 3:
			// deactivate portals
			screen.setText(new String[] { "Quest completed", "Quest: Destroy every portal" });
			ctrl.setScreen("game intro");

			map = new MapScreen(
					new LevelBuilder(Window.SIZE_X / 16, Window.SIZE_Y / 16 - 6, LevelBuilder.Layout.MAZE_WITH_ROOMS)
							.genMonster(2, 20).genChest(0, 10).genShrine(0, 2).genPortal(1, 0).create());
			objective = new Objective(Goal.PORTAL, 1); // calculate the number of portals created
			break;
		case 4:
			// default level
			screen.setText(new String[] { "Quest completed", "Quest: Kill 50 Monster" });
			ctrl.setScreen("game intro");

			map = new MapScreen(new LevelBuilder(300, 200, LevelBuilder.Layout.LOOPED_ROOMS).genMonster(1.3, 0.1)
					.genChest(0.2, 0.01).create());
			objective = new Objective(Goal.MONSTER, 50);
			break;
		case 5:
			// boss maze
			screen.setText(new String[] { "Quest completed", "Quest: Kill the BOSS" });
			ctrl.setScreen("game intro");

			map = new MapScreen(new LevelBuilder(Window.SIZE_X / 16, Window.SIZE_Y / 16 - 6, LevelBuilder.Layout.MAZE)
					.genChest(0, 0.01).genOpponent(0, 0.005).create());
			objective = new Objective(Goal.OPPONENT, 1);
			break;
		default:

		}

		// update game screens
		panel = new PanelDecorator(map);
		alert = new AlertDecorator(panel);
		ctrl.addScreen("game", alert);
		panel.updateProgress(objective.progress());
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
			if (level == 6) {
				ScreenControl.getCtrl().setScreen("game won");
			} else {
				level++;
				loadObjective(level);
			}
		}
	}

}
