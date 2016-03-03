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
		return input.contains("LEFT");
	}
	
	public boolean isRight() {
		return input.contains("RIGHT");
	}
	
	public boolean isUp() {
		return input.contains("UP");
	}
	
	public boolean isDown() {
		return input.contains("DOWN");
	}
}
