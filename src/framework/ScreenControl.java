package framework;

import java.util.HashMap;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.util.Duration;

public class ScreenControl {
	// singleton object
	private static ScreenControl singleton;

	// game state
	private HashMap<String, Screen> screens;
	private Screen screen;

	private ScreenControl() {
		screens = new HashMap<>();
	}

	public static ScreenControl getCtrl() {
		if (singleton == null) {
			singleton = new ScreenControl();
		}

		return singleton;
	}

	/**
	 * @return the state
	 */
	public Screen getScreen() {
		return screen;
	}

	/**
	 * add new screen with string id
	 * 
	 * @param name
	 * @param screen
	 */
	public void addScreen(String name, Screen screen) {
		screens.put(name, screen);
		screen.getScene().getRoot().setOpacity(0);
	}

	/**
	 * remove a screen by string id
	 * 
	 * @param name
	 */
	public void removeScreen(String name) {
		screens.remove(name);
	}

	/**
	 * set a screen by string id
	 * 
	 * @param name
	 */
	public void setScreen(String name) {
		Timeline animation;

		// dont try to set new screen
		if (screens.get(name) == null) {
			System.out.println("invalid screen");
			return;
		}

		// fade out animation
		if (screen != null) {
			DoubleProperty opacity = screen.getScene().getRoot().opacityProperty();
			animation = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
					new KeyFrame(new Duration(800), new KeyValue(opacity, 0.0)));
			animation.play();
			animation.setOnFinished(e -> {
				screen = screens.get(name);
				Window.setScene(screen.getScene());

				DoubleProperty newOpacity = screen.getScene().getRoot().opacityProperty();
				new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(newOpacity, 0.0)),
						new KeyFrame(new Duration(800), new KeyValue(newOpacity, 1.0))).play();
			});
		} else {
			screen = screens.get(name);
			Window.setScene(screen.getScene());

			DoubleProperty newOpacity = screen.getScene().getRoot().opacityProperty();
			new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(newOpacity, 0.0)),
					new KeyFrame(new Duration(2500), new KeyValue(newOpacity, 1.0))).play();
		}
	}

	/**
	 * set a new screen by giving it a name
	 * 
	 * @param name
	 * @param screen
	 */
	public void setScreen(String name, Screen screen) {
		// if scene id is already used, dont set screen
		if (screens.get(name) != null) {
			return;
		}

		// add screen first and then set it
		addScreen(name, screen);
		setScreen(name);
	}
}
