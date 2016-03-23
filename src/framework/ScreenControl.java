package framework;

import java.util.HashMap;

public class ScreenControl {
	// singleton object
	private static ScreenControl singleton;

	// game state
	private HashMap<String, Screen> screens;
	private Screen screen;

	private ScreenControl() {
		screens = new HashMap<>();
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
	public Screen getScreen() {
		return screen;
	}

	/**
	 * add new screen with string id
	 * 
	 * @param name
	 * @param screen
	 */
	public void addScreen(String name, Screen screen) {
		screens.put(name, screen);
	}

	/**
	 * remove a screen by string id
	 * 
	 * @param name
	 */
	public void removeScreen(String name) {
		screens.remove(name);
	}

	/**
	 * set some screen active
	 * 
	 * @param name
	 */
	public void setScreen(String name) {
		if (screens.get(name) != null) {
			screen = screens.get(name);
			Window.setScene(screen.getScene());
		}
	}
}
