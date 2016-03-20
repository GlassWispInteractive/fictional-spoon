package framework;

public class StateHelp extends State {
	// singleton
	private static StateHelp singleton;

	private StateHelp() {
		super();
	}
	
	/**
	 * static method to get the singleton class object
	 * 
	 * @return
	 */
	public static StateHelp getControl() {
		if (singleton == null) {
			singleton = new StateHelp();
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
