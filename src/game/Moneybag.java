package game;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Moneybag {
	private static Moneybag instance;

	private Sprite briefcase;
	private ArrayList<Sprite> moneybagList;
	private int score;

	private Moneybag() {
		briefcase = new Sprite();
		briefcase.setImage("/resources/briefcase.png");
		briefcase.setPosition(200, 0);

		moneybagList = new ArrayList<Sprite>();

		for (int i = 0; i < 15; i++) {
			Sprite moneybag = new Sprite();
			moneybag.setImage("/resources/moneybag.png");
			double px = 350 * Math.random() + 50;
			double py = 350 * Math.random() + 50;
			moneybag.setPosition(px, py);
			moneybagList.add(moneybag);
		}
	}

	public static Moneybag getBag() {
		if (instance == null) {
			instance = new Moneybag();
		}

		return instance;
	}

	public void tick(double elapsedTime) {
		// game logic
		briefcase.setVelocity(0, 0);
		Events e = Events.getEvents();
		if (e.isLeft())
			briefcase.addVelocity(-50, 0);
		if (e.isRight())
			briefcase.addVelocity(50, 0);
		if (e.isUp())
			briefcase.addVelocity(0, -50);
		if (e.isDown())
			briefcase.addVelocity(0, 50);

		briefcase.update(elapsedTime);

		// collision detection
		Iterator<Sprite> moneybagIter = moneybagList.iterator();
		while (moneybagIter.hasNext()) {
			Square moneybag = moneybagIter.next();
			if (briefcase.intersects(moneybag)) {
				moneybagIter.remove();
				score++;
			}
		}
	}

	public void render(GraphicsContext gc) {
		Font theFont = Font.font("Helvetica", FontWeight.BOLD, 24);
		gc.setFont(theFont);
		gc.setFill(Color.GREEN);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(1);
		
		briefcase.render(gc);

		gc.setFill(Color.CADETBLUE);
		for (Square moneybag : moneybagList)
			moneybag.render(gc);

		String pointsText = "Cash: $" + (100 * score);
		gc.setFill(Color.ALICEBLUE);
		gc.fillText(pointsText, 560, 36);
		gc.strokeText(pointsText, 560, 36);
	}
}
