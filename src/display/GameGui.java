package display;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GameGui extends JPanel {
	// private CellularAutomaton automaton;
	private Level level;


	GameGui(Level level){
		this.level = level;
	}



	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;


		if(level != null){
			//level.drawCompleteMap(g2);
			level.drawPlayerView(g2);
		}
	}

}