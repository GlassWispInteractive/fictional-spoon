package screens;

import java.util.ArrayList;
import java.util.Random;

import framework.Consts;
import framework.Screen;
import framework.ScreenDecorator;
import javafx.scene.canvas.GraphicsContext;

public class SoulsDecorator extends ScreenDecorator {
	private static ArrayList<double[]> souls;
	private static int soulWait;
	private static Random rand = new Random();

	public SoulsDecorator(Screen decoratedScreen) {
		super(decoratedScreen);

		// background souls
		souls = new ArrayList<>();
		soulWait = 0;

		for (int i = 0; i < 10; i++) {
			final int x = rand.nextInt(Consts.SIZE_X - 200), y = rand.nextInt(Consts.SIZE_Y - 200);
			souls.add(new double[] { 100 + x, 100 + y });
		}

		addLayer("souls", 0, 0, Consts.SIZE_X, Consts.SIZE_Y);
	}

	public void tick(int ticks) {
		// call decorated screen
		super.tick(ticks);

		// soul computation
		if (soulWait > 0) {
			soulWait -= ticks;
		} else {
			soulWait = 15;

			for (double[] soul : souls) {
				soul[0] += (-1) + rand.nextInt(3);
				if (soul[0] < 50)
					soul[0] = 50;
				if (soul[0] > Consts.SIZE_X - 50)
					soul[0] = Consts.SIZE_X - 50;

				soul[1] += (-1) + rand.nextInt(3);
				if (soul[1] < 50)
					soul[1] = 50;
				if (soul[1] > Consts.SIZE_Y - 50)
					soul[1] = Consts.SIZE_Y - 50;
			}
		}
	}

	public void render() {
		// call decorated screen
		super.render();

		// render souls
		GraphicsContext gc = gcs.get("souls");
		gc.clearRect(0, 0, layers.get("souls").getWidth(), layers.get("souls").getHeight());

		// render background souls
		for (double[] soul : souls) {
			gc.setFill(Consts.RED.deriveColor(2, 1.2, 1, 0.3));
			gc.fillOval(soul[0], soul[1], 25, 25);
		}
	}
}
