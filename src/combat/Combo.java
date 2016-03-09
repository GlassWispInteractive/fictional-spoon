package combat;

public class Combo {
	private Element[] combo;
	
	public Combo(Element[] combo) {
		this.setCombo(combo);
	}

	/**
	 * @return the combo
	 */
	public Element[] getCombo() {
		return combo;
	}

	/**
	 * @param combo the combo to set
	 */
	public void setCombo(Element[] combo) {
		this.combo = combo;
	}
	
	
}
