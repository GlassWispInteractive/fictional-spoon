package framework;

import java.util.HashSet;
import java.util.Set;

import entities.EntityFactory;
import gameviews.AlertView;
import gameviews.ComboView;
import gameviews.GameView;
import gameviews.InfoView;
import gameviews.MapView;
import generation.Map;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;

public class GameControl extends State {
	// singleton
	private static GameControl singleton;

	// class components
	protected enum Views {
		COMBAT, COMBO, MAP
	};

	private Set<GameView> collection;

	private MapView mapView;
	private ComboView comboView;
	private InfoView infoView;

	private AlertView alertView;

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
	
	public static void resetGame(){
	    //reset EntityFactory
	    EntityFactory.resetGame();	    
	    
	    singleton = new GameControl();
	}
	
	private GameControl() {
		// call the very important state constructor
		super();
		
//		add new subgroup
		StackPane center = new StackPane();
		
		center.setPrefWidth(Window.SIZE_X);
		center.minHeight(Window.SIZE_Y - 100);
		center.maxHeight(Window.SIZE_Y - 100);
		group.getChildren().add(center);
		center.relocate(0, 50);
		
		collection = new HashSet<GameView>();
		
		stackLayer(center, "combo", 0, 0, Window.SIZE_X, Window.SIZE_Y - 100);
		comboView = new ComboView(layers.get("combo"));
		collection.add(comboView);

//		addLayer("info", 0, Window.SIZE_Y - 50, Window.SIZE_X, 50);
//		infoView = new InfoView(layers.get("info"));
//		collection.add(infoView);
		
		addLayer("alert", 0, 300, Window.SIZE_X, 100);
		alertView = new AlertView(layers.get("alert"));
		alertView.push("Walk with WASD");
		collection.add(alertView);
		
		
		
//		final int size = 16;
//		stackLayer(center, "map", 0, 0, 350 * size, 225 * size);
//		stackLayer(center, "entities", 0, 0, Window.SIZE_X, Window.SIZE_Y - 100);
//		mapView = new MapView(layers.get("map"), layers.get("entities"));
//		collection.add(mapView);
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
