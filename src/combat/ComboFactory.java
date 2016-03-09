package combat;

import java.util.ArrayList;

public class ComboFactory {
	private static ComboFactory singleton;
	private ArrayList<Combo> combos;
	
	
	private ComboFactory() {
		combos = new ArrayList<>();
	}
	
	public static ComboFactory getFac() {
		if (singleton == null) {
			singleton = new ComboFactory();
		}
		
		return singleton;
	}
	
	public Combo[] getCombos() {
		return combos.toArray(new Combo[]{});
	}
	
	public Combo makeCombo(Element[] elements) {
		Combo combo = new Combo(elements);
		combos.add(combo);
		return combo;
	}
}
