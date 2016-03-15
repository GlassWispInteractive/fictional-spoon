package game;

import static game.State.*;

import gen.environment.Ground;
import javafx.scene.paint.Paint;

public class StateController {
	private static StateController singleton;

	// game state
	private State state;

	// static colors
	private static Paint[] groundColor = { Paint.valueOf("#212121"), Paint.valueOf("#A1D490"), Paint.valueOf("#D4B790"),
			Paint.valueOf("#9C7650"), Paint.valueOf("#801B1B"), Paint.valueOf("#000000") };

	private StateController() {
		state = MENU;
	}

	public static StateController getGame() {
		if (singleton == null) {
			singleton = new StateController();
		}

		return singleton;
	}

	/**
	 * @return the state
	 */
	public State getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(State state) {
		this.state = state;
	}

	public boolean isMenu() {
		return state == MENU;
	}

	public static Paint getColor(Ground ground) {
		return groundColor[ground.ordinal()];
	}

}
