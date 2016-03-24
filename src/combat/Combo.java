package combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.stream.Collectors;

public class Combo {
	private static Random random = new Random();
	
	private static ArrayList<Combo> combos = new ArrayList<>();
	private static HashSet<Combo> combosInUse = new HashSet<>();

	private Element[] combo;

	private Combo(Element[] combo) {
		this.setCombo(combo);
		combos.add(this);
		combosInUse.add(this);
		// System.out.println(Arrays.toString(combo));
	}
	
	/**
	 * Random generatedable object
	 * @param lenT
	 * @return
	 */
	public static Combo generate(int lenT) {
		Element[] elements = new Element[lenT];
		elements = Arrays.stream(elements).map(e -> Element.values()[random.nextInt(Element.values().length)]).toArray(Element[]::new);

		return new Combo(elements);
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
	 * clear the combo list
	 */
	public static void clear() {
		// delete every combo which is not yet activated
		combos = (ArrayList<Combo>) combos.stream().filter(e -> combosInUse.contains(e)).collect(Collectors.toList());
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

	@Override
	public String toString() {
		return toString(combo);

	}

	public static String toString(Element[] elements) {
		// write each element as the first latter
		return Arrays.stream(elements).map(e -> e.toString().substring(0, 1)).collect(Collectors.joining("-"));
	}
}
