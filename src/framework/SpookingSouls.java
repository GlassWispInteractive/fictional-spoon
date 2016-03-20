package framework;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SpookingSouls {
	private static SpookingSouls singleton;

	// decorated object
	private Canvas canvas;

	private static ArrayList<double[]> souls;
	private static int soulWait;
	private Random rand = new Random();

	private SpookingSouls() {
		// background souls
		souls = new ArrayList<>();
		soulWait = 0;

		for (int i = 0; i < 25; i++) {
			int x = rand.nextInt(Window.SIZE_X - 200), y = rand.nextInt(Window.SIZE_Y - 200);
			souls.add(new double[] { 100 + x, 100 + y });
		}
	}

	public static SpookingSouls getObject() {
		if (singleton == null) {
			singleton = new SpookingSouls();
		}

		return singleton;
	}

	public void tick(int ticks) {
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
	}

	public void render(GraphicsContext gc) {
		// render background souls
		for (double[] soul : souls) {
			gc.setFill(Color.DARKRED.deriveColor(2, 1.2, 1, 0.3));
			gc.fillOval(soul[0], soul[1], 25, 25);
		}
	}
}
