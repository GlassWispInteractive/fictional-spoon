package core;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Context {
	// game title
	public static final String TITLE = "fictional spoon";

	// sizes
	public static final int WINDOW_WIDTH = 1200;
	public static final int WINDOW_HEIGHT = 800;

	public static final int TILE_SIZE = 16;
	public static final int PANEL_HEIGHT = 3 * TILE_SIZE;
	public static final int GAME_WIDTH = WINDOW_WIDTH;
	public static final int GAME_HEIGHT = WINDOW_HEIGHT - 2 * PANEL_HEIGHT;

	// fonts
	public static final Font HUGE_FONT = Font.font("Helvetica", FontWeight.BOLD, 64);
	public static final Font BIG_FONT = Font.font("Helvetica", FontWeight.BOLD, 32);
	public static final Font DEFAULT_FONT = Font.font("Helvetica", FontWeight.BOLD, 24);
	public static final Font SMALL_FONT = Font.font("Helvetica", FontWeight.NORMAL, 16);

	// colors
	public static final Color WHITE = (Color) Paint.valueOf("#CFCFCF");
	public static final Color DARKGRAY = (Color) Paint.valueOf("#212121");
	public static final Color RED = (Color) Paint.valueOf("#E62929");
	public static final Color DARKRED = (Color) Paint.valueOf("#801B1B");;
	public static final Color ORANGE = (Color) Paint.valueOf("#D1AC5C");
	public static final Color GREEN = (Color) Paint.valueOf("#29E66B");

	private Context() {
		throw new AssertionError("no objects of this class available");
	}

}
