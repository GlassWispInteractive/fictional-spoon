package framework;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import org.omg.PortableServer.POAManagerPackage.State;

import combat.Combat;
import combat.Combo;
import entities.Entity;
import entities.EntityFactory;

public class Window extends Application {
	// public window wide settings
	public static final int SIZE_X = 1392, SIZE_Y = 896;
	public static final Font bigFont = Font.font("Helvetica", FontWeight.BOLD, 24);
	public static final Font smallFont = Font.font("Helvetica", FontWeight.NORMAL, 16);
	public static final Paint[] groundColor = { Paint.valueOf("#212121"), Paint.valueOf("#A1D490"),
			Paint.valueOf("#D4B790"), Paint.valueOf("#9C7650"), Paint.valueOf("#801B1B"), Paint.valueOf("#000000") };

	// make the stage acessible
	private static Scene scene;
	private static boolean newStage = false;

	// class members
	private AnimationTimer gameloop;
	private int passedTicks = 0;
	private double lastNanoTime = System.nanoTime();
	private double time = 0;

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

		Menu menu = new Menu();
		menu.setList(new String[] { "Start", "Combat", "Help", "Credits", "Exit" });

		gameloop = new AnimationTimer() {

			public void handle(long currentNanoTime) {
				// calculate time since last update.
				time += (currentNanoTime - lastNanoTime) / 1000000000.0;
				lastNanoTime = currentNanoTime;
				passedTicks = (int) Math.floor(time * 60.0);
				time -= passedTicks / 60.0;

				// adjust stage if necessary
				if (Window.newStage) {
					newStage = false;
					stage.setScene(scene);
				}

				// compute a frame
				menu.start();

				//

				// switch (game.getState()) {
				// case MENU:
				// stage.setScene(menu.getScene());
				// menu.tick(passedTicks);
				// menu.render();
				// if (menu.isStarted()) {
				// Entity player = EntityFactory.getFactory().getPlayer();
				// // lvl.updateView();
				// lvl.initCamera(player.getX(), player.getY());
				// game.setState(StateName.VIEW);
				// }
				// break;
				//
				// case MAP:
				// case VIEW:
				// stage.setScene(lvl.getScene());
				// lvl.tick(time);
				// lvl.render();
				// break;
				//
				// case COMBAT:
				//// stage.setScene(combat.getScene());
				//// combat.tick(passedTicks);
				//// combat.render();
				// break;
				//
				// default:
				// throw new IllegalArgumentException("Unknown game state: " +
				// game.getState());
				// }
			}
		};

		stage.show();
		gameloop.start();
	}

	public static void setScene(Scene scene) {
		Window.newStage = true;
		Window.scene = scene;
	}
}
