package framework;

import java.util.HashMap;



public class ScreenControl {
	private static ScreenControl singleton;

	// game state
	private HashMap<String, Screen> screens = new HashMap<>();

	private Screen screen;

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
		return screen;
	}

	public void tick(int ticks) {
		screen.tick(ticks);
	}

	public void render() {
		screen.render();
	}

	public void addScreen(String name, Screen screen) {
		screens.put(name, screen);
	}

	public void removeScreen(String name) {
		screens.remove(name);
	}
	
	public void setScreen(String name) {
		screen = screens.get(name);
		Window.setScene(screen.getScene());
	}

}
