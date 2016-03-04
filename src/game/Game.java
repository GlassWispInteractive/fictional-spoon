package game;

import static game.State.*;

public class Game {
	private static Game singleton;
	private State state;

	private Game() {
		state = MENU;
	}

	public static Game getGame() {
		if (singleton == null) {
			singleton = new Game();
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
}
