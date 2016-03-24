package framework;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import screens.ComboScreen;
import screens.CreditsScreen;
import screens.FinishScreen;
import screens.HelpScreen;
import screens.MenuScreen;
import screens.SoulsDecorator;

public class Window extends Application {
	// public window wide settings
	public static final String TITLE = "fictional spoon";
	public static final int SIZE_X = 1200, SIZE_Y = 800;
	public static final Font HUGE_FONT = Font.font("Helvetica", FontWeight.BOLD, 64);
	public static final Font BIG_FONT = Font.font("Helvetica", FontWeight.BOLD, 32);
	public static final Font DEFAULT_FONT = Font.font("Helvetica", FontWeight.BOLD, 24);
	public static final Font SMALL_FONT = Font.font("Helvetica", FontWeight.NORMAL, 16);
	public static final Paint[] GROUND_COLOR = { Paint.valueOf("#212121"), Paint.valueOf("#A1D490"),
			Paint.valueOf("#D4B790"), Paint.valueOf("#9C7650"), Paint.valueOf("#801B1B"), Paint.valueOf("#000000") };
	public static boolean music = false;

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
		stage.setTitle(TITLE);
		stage.setResizable(false);
		stage.centerOnScreen();

		// event handling
		stage.setOnCloseRequest(event -> {
			gameloop.stop();
			// save game state here
		});

		stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			EventControl.getEvents().addCode(event);
		});

		stage.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
			EventControl.getEvents().removeCode(event);
		});

		ScreenControl ctrl = ScreenControl.getCtrl();
		ctrl.setScreen("menu", new SoulsDecorator(MenuScreen.getScreen()));
		ctrl.addScreen("combo", new ComboScreen());
		ctrl.addScreen("credits", new SoulsDecorator(CreditsScreen.getScreen()));
		ctrl.addScreen("help", new SoulsDecorator(HelpScreen.getScreen()));

		ctrl.addScreen("game won", new SoulsDecorator(new FinishScreen(true)));
		ctrl.addScreen("game over", new SoulsDecorator(new FinishScreen(false)));
		// ctrl.addScreen("", );

		MenuScreen.getScreen().setList(new String[] { "Start", "Credits", "Help", "Exit" });

		// precompute the game initialization
		GameControl.getControl();

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

				if (EventControl.getEvents().isESC()) {
					ctrl.setScreen("menu");
				}

				// compute a frame
				ctrl.tick(passedTicks);
				ctrl.render();
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
