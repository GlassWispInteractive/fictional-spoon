package game;

import static game.State.*;

import gen.environment.Ground;
import javafx.scene.paint.Paint;

public class Game {
	private static Game singleton;
	
//	game state
	private State state;
	
//	static colors
	private static Paint[] groundColor = { Paint.valueOf("#212121"), Paint.valueOf("#A1D490"), Paint.valueOf("#D4B790"),
			Paint.valueOf("#9C7650"), Paint.valueOf("#801B1B"), Paint.valueOf("#000000") };

	private Game() {
//		Entity player = EntityFactory.getFactory().getPlayer();
//		World lvl = World.getWorld();
//		lvl.updateView();
//		lvl.initView(player.getX(), player.getY());
		state = MENU;
	}

	public static Game getGame() {
		if (singleton == null) {
			singleton = new Game();
		}

		return singleton;
	}

	/**
	 * @return the state
	 */
	public State getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(State state) {
		this.state = state;
	}

	public boolean isMenu() {
		return state == MENU;
	}
	
	
	public static Paint getColor(Ground ground) {
	     return groundColor[ground.ordinal()];
	}
	
}
