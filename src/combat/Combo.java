package combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Combo {
	private static ArrayList<Combo> combos = new ArrayList<>();
	private static HashSet<Combo> combosInUse = new HashSet<>();

	private Element[] combo;

	public Combo(Element[] combo) {
		this.setCombo(combo);
		combos.add(this);
		
		System.out.println(Arrays.toString(combo));
	}
	
	public static Combo random(int len) {
		Element[] elements = new Element[len];
		System.out.println(Arrays.stream(elements).map(e -> Element.FIRE).toArray().toString());
		System.out.println(Arrays.toString(elements));
		
		return null;
	}

	/**
	 * @return the combos
	 */
	public static ArrayList<Combo> getCombos() {
		return combos;
	}

	/**
	 * @return the combosInUse
	 */
	public static HashSet<Combo> getCombosInUse() {
		return combosInUse;
	}

	/**
	 * @return the combo
	 */
	public Element[] getCombo() {
		return combo;
	}

	/**
	 * @param combo
	 *            the combo to set
	 */
	public void setCombo(Element[] combo) {
		this.combo = combo;
	}

	/**
	 * activate the combo for the player
	 */
	public void activate() {
		combosInUse.add(this);
	}
}
