package game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import moneybag.Moneybag;

import static game.State.*;

import entities.Entity;
import entities.EntityFactory;

public class Window extends Application {
	// constants
	private final int SIZE_X = 1400, SIZE_Y = 900;

	// class members
	private AnimationTimer gameloop;

	private Game game;

	private double blockTime = 0;

	private World lvl;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		game = Game.getGame();
		lvl = World.getWorld();
		Menu.getMenu().setList(new String[] { "Start", "Reset", "Help", "Credits", "Exit" });

		// root objects
		Group root = new Group();
		Scene scene = new Scene(root, SIZE_X, SIZE_Y, Paint.valueOf("#454545"));

		// main stage settings
		stage.setScene(scene);
		stage.setTitle("Fictional Spoon");
		stage.setResizable(false);

		stage.setOnCloseRequest(event -> {
			gameloop.stop();
			// save game state here
			// System.out.println("game is saved");
		});

		Canvas canvas = new Canvas(1400, 900);
		canvas.setCache(true);
		// canvas.setCacheShape(true);
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

		gameloop = new AnimationTimer() {
			private long lastNanoTime = System.nanoTime();
			private double elapsedTime = 0;

			public void handle(long currentNanoTime) {
				// calculate time since last update.
				elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
				lastNanoTime = currentNanoTime;

				if (blockTime > 0) {
					blockTime -= elapsedTime;
					return;
				}

				if (Events.getEvents().isESC()) {
					game.setState(MENU);
				}
				if (Events.getEvents().isM()) {
					if (game.getState() == MAP) {
						Entity player = EntityFactory.getFactory().getPlayer();
						
						game.setState(VIEW);;
						lvl.updateView();
						lvl.initView(player.getX(), player.getY());
					} else {
						game.setState(MAP);
						lvl.updateView();
					}
					Events.getEvents().clear();
				}

				// compute a frame
				gc.clearRect(0, 0, 1400, 900);

				switch (game.getState()) {
				case MENU:
					Menu.getMenu().tick(elapsedTime);
					Menu.getMenu().render(gc);
					if (Menu.getMenu().isStarted()) {
						Entity player = EntityFactory.getFactory().getPlayer();
						lvl.updateView();
						lvl.initView(player.getX(), player.getY());
						game.setState(VIEW);
					}
					break;

				case MONEYBAG:
					Moneybag.getBag().tick(elapsedTime);
					Moneybag.getBag().render(gc);
					break;

				case MAP:
				case VIEW:
					lvl.tick(elapsedTime);
					lvl.render(gc);
					break;

				default:
					throw new IllegalArgumentException("Unknown game state: " + game.getState());
				}
			}
		};

		stage.show();
		gameloop.start();
	}
}
