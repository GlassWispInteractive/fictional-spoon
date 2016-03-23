package framework;

import java.util.HashSet;
import java.util.Set;

import entities.EntityFactory;
import generation.Map;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import screens.AlertDecorator;
import screens.ComboScreen;
import screens.GameView;
import screens.PanelDecorator;
import screens.MapScreen;

public class GameControl extends Screen {
	// singleton
	private static GameControl singleton;

	// class components
	protected enum Views {
		COMBAT, COMBO, MAP
	};

	private Set<GameView> collection;

	private MapScreen mapView;
	private ComboScreen comboView;
	private PanelDecorator infoView;

	private AlertDecorator alertView;

	/**
	 * static method to get the singleton class object
	 * 
	 * @return
	 */
	public static GameControl getControl() {
		if (singleton == null) {
			singleton = new GameControl();
		}
		return singleton;
	}

	public static void resetGame() {
		// reset EntityFactory
		EntityFactory.resetGame();

		singleton = new GameControl();
	}

	private GameControl() {
		// call the very important state constructor
		super();

		// add new subgroup

		GridPane grid = new GridPane();

		StackPane center = new StackPane();

		group.getChildren().add(grid);

		collection = new HashSet<GameView>();

		stackLayer(center, "combo", 0, 0, Window.SIZE_X, Window.SIZE_Y - 100);
		comboView = new ComboScreen(layers.get("combo"));
		collection.add(comboView);

		stackLayer(grid, "info", 0, Window.SIZE_Y - 50, Window.SIZE_X, 50);
		infoView = new PanelDecorator(layers.get("info"));
		collection.add(infoView);

		addLayer("alert", 0, 300, Window.SIZE_X, 100);
		alertView = new AlertDecorator(layers.get("alert"));
		alertView.push("Walk with WASD");
		collection.add(alertView);

		// final int size = 16;
		//
		// stackLayer(center, "entities", 0, 0, Window.SIZE_X, Window.SIZE_Y -
		// 100);
		// stackLayer(center, "map", 0, 0, 350 * size, 225 * size);
		//
		// mapView = new MapView(layers.get("map"), layers.get("entities"));
		// collection.add(mapView);

		grid.getChildren().add(center);
		// grid.getChildren().add(layers.get("info"));

		// center.setSnapToPixel(true);
		center.relocate(0, 50);
		layers.get("info").relocate(0, Window.SIZE_Y - 50);
		center.setPrefWidth(Window.SIZE_X);
		center.minHeight(Window.SIZE_Y - 50);
		center.maxHeight(Window.SIZE_Y - 50);

		System.out.println(center.getMaxHeight());

	}

	/**
	 * @return the map
	 */
	public Map getMap() {
		// return map;
		return mapView.getMap();
	}

	/**
	 * method is called every tick
	 * 
	 * @param ticks
	 */
	@Override
	public void tick(int ticks) {
		for (GameView view : collection) {
			view.tick(ticks);
		}
	}

	@Override
	protected void render() {
		for (GameView view : collection) {
			view.render();
		}
	}

	public void updateCamera(int x, int y) {
		mapView.updateCamera(x, y);
	}

	public void alert(String string) {
		alertView.push(string);
	}

}
