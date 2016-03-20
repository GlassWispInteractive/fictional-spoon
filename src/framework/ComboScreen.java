package framework;

import java.util.ArrayList;

import com.sun.javafx.tk.Toolkit;

import combat.Combo;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class ComboScreen extends State {

	private static ComboScreen singleton;

	public static ComboScreen getComboScreen() {

		if (singleton == null) {
			singleton = new ComboScreen();
		}

		return singleton;
	}

	private ComboScreen() {

	}

	@Override
	protected void tick(int ticks) {

		computeBackgroundSouls(ticks);

	}

	@Override
	protected void render() {
	}
}
