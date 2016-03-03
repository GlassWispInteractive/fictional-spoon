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

public class Window extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage theStage) {
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

		ArrayList<Square> moneybagList = new ArrayList<Square>();

		for (int i = 0; i < 15; i++) {
			Square moneybag = new Square();
//			moneybag.setImage("/resources/moneybag.png");
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

				Iterator<Square> moneybagIter = moneybagList.iterator();
				while (moneybagIter.hasNext()) {
					Square moneybag = moneybagIter.next();
					if (briefcase.intersects(moneybag)) {
						moneybagIter.remove();
						score++;
					}
				}

				// render

				gc.clearRect(0, 0, 1400, 900);
				briefcase.render(gc);

				for (Square moneybag : moneybagList)
					moneybag.render(gc);

				String pointsText = "Cash: $" + (100 * score);
				gc.fillText(pointsText, 560, 36);
				gc.strokeText(pointsText, 560, 36);
			}
		}.start();

		theStage.show();
	}

	// @SuppressWarnings("unused")
	// private Window() {
	// // Sets the title for this frame.
	// this.setTitle("in my mind - there is a spoon");
	//
	// // Sets size of the frame.
	// if (false) // Full screen mode
	// {
	// // Disables decorations for this frame.
	// this.setUndecorated(true);
	// // Puts the frame to full screen.
	// this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	// } else // Window mode
	// {
	// // Size of the frame.
	// this.setSize(1400, 900);
	// // Puts frame to center of the screen.
	// this.setLocationRelativeTo(null);
	// // So that frame cannot be resizable by the user.
	// this.setResizable(false);
	// }
	//
	// // Exit the application when user close frame.
	// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//
	// // Creates the instance of the Framework.java that extends the
	// // Canvas.java and puts it on the frame.
	// this.setContentPane(new Framework());
	//
	// this.setVisible(true);
	// }

}
