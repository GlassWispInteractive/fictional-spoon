package framework;

import combat.Combo;
import combat.Goal;
import combat.Objective;
import entities.Entity;
import entities.Portal;
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

	private GameControl() {
		// call the very important state constructor
		super();

		// settings
		screen = new HelpScreen("game", 180);
		ScreenControl.getCtrl().addScreen("game intro", screen);
		level = 3;
		startArcade(level);

	}

	/**
	 * @return the objective
	 */
	public String getQuest() {
		return objective.toString();
	}

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

	/**
	 * reset the game state
	 */
	public static void resetGame() {
		// reset EntityFactory
		Entity.reset();
		Combo.resetCombos();

		singleton = new GameControl();
	}

	private void startArcade(int level) {
		//
		ScreenControl ctrl = ScreenControl.getCtrl();

		if (level > 1) {
			// reset stuff
			ctrl.setScreen("game intro");

			Entity.reset();
			Combo.resetCombos();
		}

		// set the appropriate objective by level
		switch (level) {
		case 1:
			// basic level
			map = new MapScreen(new LevelBuilder(Global.GAME_WIDTH / 16, Global.GAME_HEIGHT / 16,
					LevelBuilder.Layout.SINGLE_CONN_ROOMS).genMonster(1, 0.01).genShrine(0.1, 0).create());
			objective = new Objective(Goal.SHRINE, 1);
			break;
		case 2:
			// default level

			map = new MapScreen(new LevelBuilder(300, 200, LevelBuilder.Layout.LOOPED_ROOMS).genMonster(1.3, 0.1)
					.genChest(0.2, 0.01).create());
			objective = new Objective(Goal.MONSTER, 30);
			break;
		case 3:
			// deactivate portals
			ctrl.setScreen("game intro");

			map = new MapScreen(new LevelBuilder(Global.GAME_WIDTH / 16, Global.GAME_HEIGHT / 16,
					LevelBuilder.Layout.MAZE_WITH_ROOMS).genMonster(2, 0.1).genChest(0, 0.05).genShrine(0, 0.01)
							.genPortal(1, 0).create());
			objective = new Objective(Goal.PORTAL, Portal.getCount()); // calculate the number
														// of portals created
			break;
		case 4:
			// default level
			ctrl.setScreen("game intro");

			map = new MapScreen(new LevelBuilder(300, 200, LevelBuilder.Layout.LOOPED_ROOMS).genMonster(1.3, 0.1)
					.genChest(0.2, 0.01).create());
			objective = new Objective(Goal.MONSTER, 50);
			break;
		case 5:
			// boss maze
			ctrl.setScreen("game intro");

			map = new MapScreen(
					new LevelBuilder(Global.GAME_WIDTH / 16, Global.GAME_HEIGHT / 16, LevelBuilder.Layout.MAZE)
							.genChest(0, 0.01).genOpponent(0, 0.005).create());
			objective = new Objective(Goal.OPPONENT, 1);
			break;
		default:

		}

		screen.setText(new String[] { "Quest: " + objective });

		// update game screens
		panel = new PanelDecorator(map);
		alert = new AlertDecorator(panel);
		ctrl.addScreen("game", alert);
		panel.updateProgress(level, objective.progress());
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
			panel.updateProgress(level, objective.progress());
		} else {
			// objective is reached
			if (level == 6) {
				ScreenControl.getCtrl().setScreen("game won");
			} else {
				level++;
				startArcade(level);
			}
		}
	}

}
