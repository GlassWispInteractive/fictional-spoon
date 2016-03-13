package game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import static game.State.*;

import combat.Combat;
import entities.Entity;
import entities.EntityFactory;

public class Window extends Application {
	// public window wide settings
	public static final int SIZE_X = 1392, SIZE_Y = 896;
	public static final Font bigFont = Font.font("Helvetica", FontWeight.BOLD, 24);
	public static final Font smallFont = Font.font("Helvetica", FontWeight.NORMAL, 16);

	// class members
	private AnimationTimer gameloop;


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		// stage settings
		stage.setTitle("Soul Harvester");
		stage.setResizable(false);
		stage.centerOnScreen();

		// event handling
		stage.setOnCloseRequest(event -> {
			gameloop.stop();
			// save game state here
		});

		stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			Events.getEvents().addCode(event);
		});
		
		stage.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
			Events.getEvents().removeCode(event);
		});
		
		
		Game game = Game.getGame();
		MapController lvl = MapController.getWorld();
		Menu menu = new Menu();
		menu.setList(new String[] { "Start", "Combat", "Help", "Credits", "Exit" });

		// Canvas layerMain = new Canvas(SIZE_X, SIZE_Y);
		// Canvas layerMsg = new Canvas(100, 100);
		//
		// root.getChildren().add(layerMain);
		// root.getChildren().add(layerMsg);
		// layerMsg.relocate(100, 100);
		//
		// 
		// layerMsg.getGraphicsContext2D().setFont(font);
		// layerMsg.getGraphicsContext2D().setFill(Color.ALICEBLUE);
		// layerMsg.getGraphicsContext2D().fillText("hello", 0, 0);

		gameloop = new AnimationTimer() {
			private int passedTicks = 0;
			private double lastNanoTime = System.nanoTime();
			private double time = 0;

			public void handle(long currentNanoTime) {
				// calculate time since last update.
				time += (currentNanoTime - lastNanoTime) / 1000000000.0;
				lastNanoTime = currentNanoTime;
				passedTicks = (int) Math.floor(time * 60.0);
				time -= passedTicks / 60.0;

				if (Events.getEvents().isESC()) {
					game.setState(MENU);
				}

				// compute a frame

				switch (game.getState()) {
				case MENU:
					stage.setScene(menu.getScene());
					menu.tick(passedTicks);
					menu.render();
					if (menu.isStarted()) {
						Entity player = EntityFactory.getFactory().getPlayer();
						// lvl.updateView();
						lvl.initCamera(player.getX(), player.getY());
						game.setState(VIEW);
					}
					break;

				case MAP:
				case VIEW:
					stage.setScene(lvl.getScene());
					lvl.tick(time);
					lvl.render();
					break;

				case COMBAT:
					stage.setScene(Combat.startCombat(null, null).getScene());
					Combat.startCombat(null, null).tick(passedTicks);
					Combat.startCombat(null, null).render();
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
