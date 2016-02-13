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

	final int offsetX = 0, offsetY = 0;
	int fieldSize = 5, padding = 0, rectSize = fieldSize - padding;

	private Color[] color = { Color.decode("#A1D490"), Color.decode("#D4B790"), Color.decode("#801B1B"),

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

		for (int i = 0; i < map.getN(); i++) {
			int x = offsetY + i * fieldSize;
			for (int j = 0; j < map.getM(); j++) {
				int y = offsetX + j * fieldSize;
				g2.setColor(color[map.getGround(i, j).ordinal()]);
				g2.fillRect(x, y, rectSize, rectSize);
			}
		}
	}

}