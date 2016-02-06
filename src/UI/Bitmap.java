package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Bitmap extends JPanel {
//	private CellularAutomaton automaton;
	private boolean[][] room;
	
	final int offsetX = 0, offsetY = 0;
	int fieldSize = 6, padding = 0, rectSize = fieldSize - padding;
	
	
	
	private Color color1 = Color.decode("#3E4366");
	private Color color2 = Color.decode("#D9BFD5");
	
	
	
	public void setRoom(boolean[][] room) {
		this.room = room;
	}
	
//	Bitmap(CellularAutomaton automaton) {
//		this.automaton = automaton;
//	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

//        room = automaton.cells;
        
        if (room == null) {
			JOptionPane.showMessageDialog(null, "broken.");
			return;
		}
        
        for (int j = 0; j < room.length; j++) {
			int y = offsetY + j * fieldSize;
			for (int i = 0; i < room[j].length; i++) {
				int x = offsetX + i * fieldSize;
				g2.setColor((room[j][i]) ? color1 : color2);
				g2.fillRect(x, y, rectSize, rectSize);
			}
		}
	}

}