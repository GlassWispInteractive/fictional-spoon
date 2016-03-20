package framework;

public class HelpControl extends State {
	// singleton
	private static HelpControl singleton;

	private HelpControl() {
		super();
	}
	
	/**
	 * static method to get the singleton class object
	 * 
	 * @return
	 */
	public static HelpControl getControl() {
		if (singleton == null) {
			singleton = new HelpControl();
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
