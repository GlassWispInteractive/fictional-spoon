package screens;

import java.util.ArrayList;
import java.util.Arrays;

import framework.EventControl;
import framework.Screen;
import framework.ScreenControl;
import framework.Window;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

//import static game.State.*;

public class MenuScreen extends Screen {
	// singleton
	private static MenuScreen singleton;

	private Image logo;

	private ArrayList<String> list;
	private int cur;

	public static MenuScreen getScreen() {
		if (singleton == null) {
			singleton = new MenuScreen();
		}

		return singleton;
	}

	private MenuScreen() {
		super();

		// init
		list = new ArrayList<>();
		cur = 0;

		logo = new Image("/resources/graphics/logo.png");
	}

	public void tick(int ticks) {
		EventControl e = EventControl.getEvents();

		// soul computation
		// SpookingSouls.getObject().tick(ticks);

		// event handling
		if (e.isUp())
			cur = (cur + list.size() - 1) % list.size();

		if (e.isDown())
			cur = (cur + 1) % list.size();

		if (e.isEnter()) {
			switch (list.get(cur)) {
			case "Start":
				ScreenControl.getCtrl().setScreen("game intro");
				break;
			case "Credits":
				ScreenControl.getCtrl().setScreen("credits");
				break;
			case "Help":
				ScreenControl.getCtrl().setScreen("help");
				break;
			case "Exit":
				System.exit(0);
				break;
			default:
				break;
			}
		}

		// no double key activation
		EventControl.getEvents().clear();
	}

	public void render() {
		// start from clean screen
		final GraphicsContext gc = gcs.get("main");
		gc.clearRect(0, 0, Window.SIZE_X, Window.SIZE_Y);

		// canvas settings
		final double w = gc.getCanvas().getWidth();

		// render backGround souls
		// SpookingSouls.getObject().render(gc);

		// render logo image
		gc.drawImage(logo, (w - logo.getWidth()) / 2, 80);

		// font type
		gc.setFont(Window.DEFAULT_FONT);

		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setLineWidth(1);

		for (int i = 0; i < list.size(); i++) {
			// render box
			gc.setFill(Color.DARKGRAY.deriveColor(0, 1.2, 1, 0.6));
			gc.setStroke(Color.DARKGRAY);
			gc.fillRoundRect((w - 200) / 2, 200 + 100 * (i + 1), 200, 60, 60, 200);
			// gc.strokeRect((w - 200) / 2, 200 + 100 * (i + 1), 200, 60);

			// render text on box
			if (i != cur) {
				gc.setFill(Color.DARKRED.deriveColor(0, 1.2, 1, 0.6));
				gc.setStroke(Color.DARKRED);
			} else {
				gc.setFill(Color.RED.deriveColor(0, 1.2, 1, 0.6));
				gc.setStroke(Color.RED);
			}

			gc.fillText(list.get(i), w / 2, 200 + 100 * (i + 1) + 30);
			gc.strokeText(list.get(i), w / 2, 200 + 100 * (i + 1) + 30);
		}

	}

	public void setList(String[] strings) {
		list = new ArrayList<String>(Arrays.asList(strings));
		cur = 0;
	}
}
