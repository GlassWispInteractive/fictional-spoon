package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Map.Level;


@SuppressWarnings("serial")
public class Bitmap extends JPanel {
//	private CellularAutomaton automaton;
	private Level room;
	
	final int offsetX = 0, offsetY = 0;
	int fieldSize = 5, padding = 0, rectSize = fieldSize - padding;
	
	
	
	private Color[] color = {	Color.decode("#FFFFFF"),
								Color.decode("#FFFF00"),
								Color.decode("#FF00FF"),
								Color.decode("#FF0000"),
								Color.decode("#C0C0C0"),
								Color.decode("#808080"),
								Color.decode("#808000"),
								Color.decode("#800080"),
								Color.decode("#800000"),
								Color.decode("#00FFFF"),
								Color.decode("#00FF00"),
								Color.decode("#008080"),
								Color.decode("#008000"),
								Color.decode("#0000FF"),
								Color.decode("#000080"),
								};
	
	
	
	public void setRoom(Level room) {
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
        
        for (int j = 0; j < room.n; j++) {
			int y = offsetY + j * fieldSize;
			for (int i = 0; i < room.m; i++) {
				int x = offsetX + i * fieldSize;
//				System.out.print(room.getValue(i, j));
//				System.out.print(" - ");
//				System.out.println(color[room.getValue(i, j)]);
				g2.setColor(color[room.getValue(i, j)]);
				g2.fillRect(x, y, rectSize, rectSize);
			}
		}
	}

}