package framework;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import screens.MenuScreen;

public class Window extends Application {
	// public window wide settings
    	public static final String TITLE = "fictional spoon";
	public static final int SIZE_X = 1200, SIZE_Y = 800;
	public static final Font HUGE_FONT = Font.font("Helvetica", FontWeight.BOLD, 64);
	public static final Font BIG_FONT = Font.font("Helvetica", FontWeight.BOLD, 32);
	public static final Font NORMAL_FONT = Font.font("Helvetica", FontWeight.BOLD, 24);
	public static final Font SMALL_FONT = Font.font("Helvetica", FontWeight.NORMAL, 16);
	public static final Paint[] GROUND_COLOR = { Paint.valueOf("#212121"), Paint.valueOf("#A1D490"),
			Paint.valueOf("#D4B790"), Paint.valueOf("#9C7650"), Paint.valueOf("#801B1B"), Paint.valueOf("#000000") };
//	public static final Font hugeFont = Font.font("Helvetica", FontWeight.BOLD, 40);
//	public static final Font bigFont = Font.font("Helvetica", FontWeight.BOLD, 24);
//	public static final Font smallFont = Font.font("Helvetica", FontWeight.NORMAL, 16);

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
		
		
		
//		ScreensController mainContainer = new ScreensController();
//        mainContainer.loadScreen(ScreensFramework.screen1ID, ScreensFramework.screen1File);
//        mainContainer.loadScreen(ScreensFramework.screen2ID, ScreensFramework.screen2File);
//        mainContainer.loadScreen(ScreensFramework.screen3ID, ScreensFramework.screen3File);
//        
//        mainContainer.setScreen(ScreensFramework.screen1ID);
//        
//        Group root = new Group();
//        root.getChildren().addAll(mainContainer);
//        Scene scene = new Scene(root);
//        primaryStage.setScene(scene);
//        primaryStage.show();
		
		
		
		

		ScreenControl ctrl = ScreenControl.getCtrl();
		ctrl.addScreen("menu", MenuScreen.getControl());

		MenuScreen.getControl().setList(new String[] { "Start", "Credits", "Help", "Exit" });
		ctrl.setScreen("menu");
		
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
