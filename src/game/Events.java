package game;

import java.util.ArrayList;
import javafx.scene.input.KeyEvent;

public class Events {
	private static Events instance;
	private ArrayList<String> input = new ArrayList<String>();
	
	
	
	public static Events getEvents() {
		if (instance == null) {
			instance = new Events();
		}
		
		return instance;
	}
	
	public void addCode(KeyEvent e) {
		String code = e.getCode().toString();
		if (!input.contains(code))
			input.add(code);
	}
	
	public void removeCode(KeyEvent e) {
		String code = e.getCode().toString();
		input.remove(code);
	}
	
	public boolean isLeft() {
		return input.contains("LEFT") || input.contains("A");
	}
	
	public boolean isRight() {
		return input.contains("RIGHT") || input.contains("D");
	}
	
	public boolean isUp() {
		return input.contains("UP") || input.contains("W");
	}
	
	public boolean isDown() {
		return input.contains("DOWN") || input.contains("S");
	}
}
