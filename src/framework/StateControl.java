package framework;



public class StateControl {
	private static StateControl singleton;

	// game state
	
	
	private State state;

	// static colors
	

	private StateControl() {
//		state = StateName.MENU;
//		states = new HashMap<>();
//		states.put(MENU, value)
		
	}

	public static StateControl getCtrl() {
		if (singleton == null) {
			singleton = new StateControl();
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
	
	public void tick(int ticks) {
		state.tick(ticks);
	}
	
	public void render() {
		state.render();
	}

}
