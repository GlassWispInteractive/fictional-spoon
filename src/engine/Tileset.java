package engine;

import javafx.scene.image.Image;

public class Tileset {
	private static Tileset singleton;
	public Image tileset;
	
	private Tileset() {
		tileset = new Image("/resources/roguelikeSheet_transparent.png");
	}
	
	public static Tileset getTileset() {
		if (singleton == null) {
			singleton = new Tileset();
		}
		
		return singleton;
	}
}
