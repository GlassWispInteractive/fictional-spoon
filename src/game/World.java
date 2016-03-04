package game;

import dungeon.Generator;
import dungeon.Map;
import entities.Entity;
import entities.EntityFactory;
import entities.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class World {

	/** SINGELTON */
	private static World singleton;

	// class components
	private Generator gen;
	private Map map;
	private EntityFactory fac;
	
	/*do NOT change*/
	private State state = State.VIEW;

	// variables
	private int size = 5;
	private int offsetX, offsetY, viewSizeX, viewSizeY;

	private Paint[] color = { Paint.valueOf("#454545"), Paint.valueOf("#A1D490"), Paint.valueOf("#D4B790"),
			Paint.valueOf("#B39B7B"), Paint.valueOf("#801B1B"), Paint.valueOf("#000000") };

	public static World getWorld() {
		if (singleton == null) {

			singleton = new World();
		}
		return singleton;
	}

	private World() {
		gen = new Generator(280, 180);
		map = gen.newLevel();
		fac = EntityFactory.getFactory();
		
//		player = fac.makePlayer(15, 15);
		changeState(state);
		Player player = fac.makePlayer(80, 70);
		fac.makeChest(81, 71);
		fac.makeShrine(81, 65);
		fac.makeMonster(79, 71, 100, 50, new int[]{5, 2, 4, 1, 0}, "test1");
		fac.makeMonster(78, 71, 100, 50, new int[]{2, 2, 4, 1, 0}, "test2");
		setCurrentView(player.getX(), player.getY());
	}

	// we should delete this function - change the map would need effects in any
	// other state as well!
	public void updateMap(Map newMap) {
		this.map = newMap;
	}

	public Map getMap() {
		return this.map;
	}

	public void setSize(int size) {
		this.size = size;

	}
	
	public void changeState(State state){
		
		this.state = state;
		
		switch (this.state) {
		case MAP:
			checkOffset();
			viewSizeX = map.getN();
			viewSizeY = map.getM();
			size = 4;
			break;

		case VIEW:
			viewSizeX = 70; 			//140
			viewSizeY = 45;				//90
			size = 20;					//10
			break;
			
		default:
			break;
		
		}		
	}
	
	public void setCurrentView(int centerX, int centerY){
		this.offsetX = centerX - viewSizeX/2;
		this.offsetY = centerY - viewSizeY/2;
		
		checkOffset();
	}
	
	public void changeCurrentView(int centerX, int centerY) {
		
		int viewPaddingX = viewSizeX / 5; //20%
		int viewPaddingY = viewSizeY / 5;
		
		if(centerX - viewPaddingX < offsetX ){
			offsetX = centerX - viewPaddingX;
		}
		if(centerX + viewPaddingX - viewSizeX > offsetX){
			offsetX = centerX + viewPaddingX - viewSizeX;
		}
		if(centerY - viewPaddingY < offsetY ){
			offsetY = centerY - viewPaddingY;
		}
		if(centerY + viewPaddingY - viewSizeY > offsetY){
			offsetY = centerY + viewPaddingY - viewSizeY;
		}
		
		checkOffset();
	}

	private void checkOffset(){
		if (offsetX < 0) {
			offsetX = 0;
		}
		if (offsetY < 0) {
			offsetY = 0;
		}
		if (offsetX >= map.getN() - viewSizeX) {
			offsetX = map.getN() - viewSizeX;
		}
		if (offsetY >= map.getM() - viewSizeY) {
			offsetY = map.getM() - viewSizeY;
		}
	}
	
	
	public void tick(double el) {
		fac.getPlayer().tick(el);
		for (Entity mob : fac.getMobs()) {
			mob.tick(el);
		}
		fac.smartDeletNow();
	}

	public void render(GraphicsContext gc) {
		// set color and render ground tile
		for (int x = 0; x < viewSizeX; x++) {
			for (int y = 0; y < viewSizeY; y++) {
				gc.setFill(color[map.getGround(x + offsetX, y + offsetY).ordinal()]);
				gc.fillRect(x * size, y * size, size, size);
			}
		}
		
		for (Entity mob : fac.getMobs()) {
			mob.render(gc, size, offsetX, offsetY);
		}
		fac.getPlayer().render(gc, size, offsetX, offsetY);
	}
}
