package game.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

public class ComboAttack {
	
	
	// members
	private static HashSet<ComboAttack> combos = new HashSet<>();
	private static ArrayList<BasicAttack[]> all;

	private BasicAttack[] combo;

	public ComboAttack() {
		this.combo = all.get(0);

		this.setCombo(combo);
		all.remove(0);
	}

	/**
	 * set the level to generate a list of combinations by some hardness factor
	 * 
	 * @param len
	 */
	public static void setLength(int len) {
		// get number of elements and permutations with repetitions
		final int m = BasicAttack.values().length;
		final int n = (int) Math.pow(m, len);
		all = new ArrayList<>();

		for (int i = 0; i < n; i++) {
			BasicAttack[] elements = new BasicAttack[len];

			// calculate the notation of the number in the m-ary numberal system
			char[] s = Integer.toString(i, m).toCharArray();

			// add leading 0 to s
			if (s.length < len) {
				char[] s_new = new char[len];
				for (int k = 0; k < len - s.length; k++)
					s_new[k] = '0';
				for (int k = 0; k < s.length; k++)
					s_new[k + len - s.length] = s[k];
				s = s_new;
			}

			// convert char array to Combo
			for (int j = 0; j < len; j++) {
				elements[j] = BasicAttack.values()[s[j] - '0'];
			}

			// add next element to the list
			all.add(elements);
		}

		// shuffle the list of possible combos
		Collections.shuffle(all);
	}

	/**
	 * @return the combosInUse
	 */
	public static HashSet<ComboAttack> getCombosInUse() {
		return combos;
	}

	/**
	 * @return the combo
	 */
	public BasicAttack[] getCombo() {
		return combo;
	}

	/**
	 * @param combo
	 *            the combo to set
	 */
	public void setCombo(BasicAttack[] combo) {
		this.combo = combo;
	}

	/**
	 * activate the combo for the player
	 */
	public void activate() {
		combos.add(this);
	}

	@Override
	public String toString() {
		return toString(combo);

	}

	public static String toString(BasicAttack[] elements) {
		// write each element as the first latter
		// toString().substring(0, 1) to get the first char
		return Arrays.stream(elements).map(e -> "" + (1 + e.ordinal())).collect(Collectors.joining("-"));
	}

	public static void resetCombos() {
		combos.clear();
	}
}
