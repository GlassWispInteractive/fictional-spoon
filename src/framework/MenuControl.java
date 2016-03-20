package framework;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

//import static game.State.*;

public class MenuControl extends State {
	
	// singleton
	private static MenuControl singleton;

	private Image logo;

	private ArrayList<String> list;
	private int cur;
	private boolean started = false;
	
	public static MenuControl getControl() {
		if (singleton == null) {
			singleton = new MenuControl();
		}
		return singleton;
	}

	private MenuControl() {
		super();

		// init
		list = new ArrayList<>();
		cur = 0;

		logo = new Image("/resources/logo.png");
	}

	public void tick(int ticks) {
		started = false;

		EventControl e = EventControl.getEvents();

		// soul computation
		computeBackgroundSouls(ticks);

		// event handling
		if (e.isUp())
			cur = (cur + list.size() - 1) % list.size();

		if (e.isDown())
			cur = (cur + 1) % list.size();

		if (e.isEnter()) {
			switch (list.get(cur)) {
			case "Start":
				GameControl.getControl().start();
				break;
			case "Combos":
				ComboScreen.getComboScreen().start();
				break;
			case "Credits":
				CreditScreen.getCreditScreen().start();
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
		GraphicsContext gc = gcs.get(0);
		gc.clearRect(0, 0, Window.SIZE_X, Window.SIZE_Y);

		// canvas settings
		double w = gc.getCanvas().getWidth();
		
		//render backGround souls
		renderBackgroundSouls(gc);

		// render logo image
		gc.drawImage(logo, (w - logo.getWidth()) / 2, 80);

		// font type
		gc.setFont(Window.normalFont);

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

	public boolean isStarted() {
		return started;
	}
}
