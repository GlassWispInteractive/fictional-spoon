package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Menu {
	private static Menu singleton;
	private Random rand;
	private Image logo;

	private ArrayList<String> list;
	private int cur;
	private ArrayList<double[]> souls;
	private int soulWait;
	private boolean started = false;

	private Menu() {
		list = new ArrayList<>();
		rand = new Random();
		cur = 0;

		logo = new Image("/resources/logo.png");

		souls = new ArrayList<>();
		for (int i = 0; i < 25; i++) {
			int x = rand.nextInt(Window.SIZE_X - 200), y = rand.nextInt(Window.SIZE_Y - 200);
			souls.add(new double[] { 100 + x, 100 + y });
		}
		// souls.add(new int[] { 20, 50 });
	}

	public static Menu getMenu() {
		if (singleton == null) {
			singleton = new Menu();
		}

		return singleton;
	}

	public void tick(int ticks) {
		started = false;

		Events e = Events.getEvents();

		// soul computation
		if (soulWait > 0) {
			soulWait -= ticks;
		} else {
			soulWait = 15;

			for (double[] soul : souls) {
				soul[0] += (-1) + rand.nextInt(3);
				if (soul[0] < 50)
					soul[0] = 50;
				if (soul[0] > Window.SIZE_X - 50)
					soul[0] = Window.SIZE_X - 50;

				soul[1] += (-1) + rand.nextInt(3);
				if (soul[1] < 50)
					soul[1] = 50;
				if (soul[1] > Window.SIZE_Y - 50)
					soul[1] = Window.SIZE_Y - 50;
			}
		}

		// event handling
		if (e.isUp())
			cur = (cur + list.size() - 1) % list.size();

		if (e.isDown())
			cur = (cur + 1) % list.size();

		if (e.isEnter()) {
			switch (list.get(cur)) {
			case "Start":
				started = true;
				break;
			case "Combat":
				Game.getGame().setState(State.COMBAT);
				break;
			case "Exit":
				// TODO save?
				System.exit(0);
				break;
			default:
				break;
			}
		}

		// no double key activation
		Events.getEvents().clear();
	}

	public void render(GraphicsContext gc) {
		// canvas settings
		double w = gc.getCanvas().getWidth();

		// render background souls
		for (double[] soul : souls) {
			gc.setFill(Color.DARKRED.deriveColor(2, 1.2, 1, 0.3));
			gc.fillOval(soul[0], soul[1], 25, 25);
		}

		// render logo image
		gc.drawImage(logo, (w - logo.getWidth()) / 2, 80);

		// font type
		Font font = Font.font("Helvetica", FontWeight.BOLD, 24);
		gc.setFont(font);

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
