package game;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import static game.State.*;

public class Window extends Application {
	private Level level;
	private State state = MONEYBAD;
	
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage theStage) {
    	level = Level.getLevel();
    	
    	
		theStage.setTitle("Fictional Spoon");

		Group root = new Group();
		Scene theScene = new Scene(root);
		theStage.setScene(theScene);

		Canvas canvas = new Canvas(1400, 900);
		root.getChildren().add(canvas);

		// key events
		ArrayList<String> input = new ArrayList<String>();

		theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				String code = e.getCode().toString();
				if (!input.contains(code))
					input.add(code);
			}
		});

		theScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				String code = e.getCode().toString();
				System.out.println(code);
				input.remove(code);
			}
		});

		GraphicsContext gc = canvas.getGraphicsContext2D();

		Font theFont = Font.font("Helvetica", FontWeight.BOLD, 24);
		gc.setFont(theFont);
		gc.setFill(Color.GREEN);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(1);

		Sprite briefcase = new Sprite();
		briefcase.setImage("/resources/briefcase.png");
		briefcase.setPosition(200, 0);

		ArrayList<Sprite> moneybagList = new ArrayList<Sprite>();

		for (int i = 0; i < 15; i++) {
			Sprite moneybag = new Sprite();
			moneybag.setImage("/resources/moneybag.png");
			double px = 350 * Math.random() + 50;
			double py = 350 * Math.random() + 50;
			moneybag.setPosition(px, py);
			moneybagList.add(moneybag);
		}

		new AnimationTimer() {
			private long lastNanoTime = System.nanoTime();
			private int score = 0;

			public void handle(long currentNanoTime) {
				// calculate time since last update.
				double elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
				lastNanoTime = currentNanoTime;

				// game logic

				briefcase.setVelocity(0, 0);
				if (input.contains("LEFT"))
					briefcase.addVelocity(-50, 0);
				if (input.contains("RIGHT"))
					briefcase.addVelocity(50, 0);
				if (input.contains("UP"))
					briefcase.addVelocity(0, -50);
				if (input.contains("DOWN"))
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

				// render
				gc.clearRect(0, 0, 1400, 900);
				
				switch (state) {
				case MONEYBAD:
					briefcase.render(gc);
					
					gc.setFill(Color.CADETBLUE);
					for (Square moneybag : moneybagList)
						moneybag.render(gc);
					
					String pointsText = "Cash: $" + (100 * score);
					gc.setFill(Color.ALICEBLUE);
					gc.fillText(pointsText, 560, 36);
					gc.strokeText(pointsText, 560, 36);
					break;
				case MAP:
					level.renderMap(gc);
				case VIEW:
					level.renderPlayerView(gc);
					break;
				}				
			}
		}.start();

		theStage.show();
	}
}
