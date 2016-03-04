package game;

import javafx.geometry.Rectangle2D;

public class TextBox extends Rectangle2D {
	private String text;
	
	public TextBox(double minX, double minY, double width, double height, String text) {
		super(minX, minY, width, height);
		this.text = text;
	}
}
