package game;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;

import javafx.stage.Stage;

import static game.State.*;

public class Window extends Application {
	private Level level;
	private State state = MONEYBAD;
	
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
    	level = Level.getLevel();
    	
		stage.setTitle("Fictional Spoon");
		stage.setResizable(false);

		Group root = new Group();
		Scene scene = new Scene(root);
		stage.setScene(scene);

		Canvas canvas = new Canvas(1400, 900);
		root.getChildren().add(canvas);

		// key events
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				Events.getEvents().addCode(e);
			}
		});

		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				Events.getEvents().removeCode(e);
			}
		});

		GraphicsContext gc = canvas.getGraphicsContext2D();

		

		AnimationTimer gameloop = new AnimationTimer() {
			private long lastNanoTime = System.nanoTime();

			public void handle(long currentNanoTime) {
				// calculate time since last update.
				double elapsedTime = (currentNanoTime - lastNanoTime) / 1E9;
				lastNanoTime = currentNanoTime;

				// compute a frame
				gc.clearRect(0, 0, 1400, 900);
				
				switch (state) {
				case MONEYBAD:
					Moneybag.getBag().tick(elapsedTime);
					Moneybag.getBag().render(gc);
					break;
					
				case MAP:
					level.renderMap(gc);
					break;
					
				case VIEW:
					level.renderPlayerView(gc);
					break;
				}				
			}
		};
		
		gameloop.start();
		stage.show();
	}
}
