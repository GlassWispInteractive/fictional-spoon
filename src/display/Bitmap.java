package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dungeon.Map;

@SuppressWarnings("serial")
public class Bitmap extends JPanel {
	// private CellularAutomaton automaton;
	private Map map;

	
	public void setRoom(Map map) {
		this.map = map;
	}

	// Bitmap(CellularAutomaton automaton) {
	// this.automaton = automaton;
	// }

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		// room = automaton.cells;

		if (map == null) {
			JOptionPane.showMessageDialog(null, "broken.");
			return;
		}

		map.draw(g2);
	}

}