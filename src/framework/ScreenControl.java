package framework;

import java.util.HashMap;


public class ScreenControl {
	private static ScreenControl singleton;

	// game state
	private HashMap<String, Screen> screens = new HashMap<>();

	private Screen state;

	// static colors

	private ScreenControl() {
		// state = StateName.MENU;
		// states = new HashMap<>();
		// states.put(MENU, value)

	}

	public static ScreenControl getCtrl() {
		if (singleton == null) {
			singleton = new ScreenControl();
		}

		return singleton;
	}

	/**
	 * @return the state
	 */
	public Screen getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(Screen state) {
		this.state = state;
	}

	public void tick(int ticks) {
		state.tick(ticks);
	}

	public void render() {
		state.render();
	}

}
