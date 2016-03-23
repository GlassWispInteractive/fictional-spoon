package screens;

import framework.Screen;

public class HelpScreen extends Screen {
	// singleton
	private static HelpScreen singleton;

	private HelpScreen() {
		super();
	}
	
	/**
	 * static method to get the singleton class object
	 * 
	 * @return
	 */
	public static HelpScreen getControl() {
		if (singleton == null) {
			singleton = new HelpScreen();
		}
		return singleton;
	}

	@Override
	protected void tick(int ticks) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void render() {
		// TODO Auto-generated method stub

	}

}
