package entities;

import engine.ImageSource;
import engine.TileFactory;
import engine.TileSource;
import framework.GameControl;

import java.awt.Point;
import java.util.Random;

import combat.Goal;

import javafx.scene.canvas.GraphicsContext;

public class Portal extends Entity{
	
	private Random rnd = new Random();
	private TileFactory tileFac = TileFactory.getTilesFactory();
//	private ImageSource imgSource = new ImageSource(TileSource.MAP_TILES, 23, 7); //alternative
	private ImageSource imgSource = new ImageSource(TileSource.MAP_TILES, 40, 9);
	private ImageSource imgSourceDestroyed = new ImageSource(TileSource.MAP_TILES, 44, 9);
//	private ImageSource imgSource = new ImageSource(TileSource.MAP_TILES, 36, 1); //alternative
	private int blocked = 0;
	private int maxMonsterSpawn = 3;
	private boolean destroyed = false;

	public Portal(int x, int y, String name) {
		super(x, y);
		
		delayTicks = 1000;
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {

	    	if(destroyed) {
	    	    tileFac.drawTile(gc, imgSourceDestroyed, (x - offsetX), (y - offsetY), size);
	    	} else {
	    	    tileFac.drawTile(gc, imgSource, (x - offsetX), (y - offsetY), size);
	    	}
	}

	@Override
	public void tick(int ticks) {
		
		if (blocked >= 0) {
			blocked -= ticks;
		}
		
		if (blocked < 0 && !destroyed) {
			
			blocked = delayTicks;
						
			spawnMonster();
			
		}
		
		EntityFactory fac = EntityFactory.getFactory();
		if (x == fac.getPlayer().getX() && y == fac.getPlayer().getY()) {

		    destroyed = true;
		    
		}
		
	}
	
	private void spawnMonster() {
		
		int directionIndex = rnd.nextInt(Direction.values().length);
	
		Direction dir;
		Point spawnPoint;

		do{
			
			dir = Direction.values()[directionIndex];
			
			spawnPoint = dir.move(new Point(x, y));
			
			directionIndex = (++directionIndex) % Direction.values().length;
			
		} while (!GameControl.getControl().getMap().isWalkable(spawnPoint.x, spawnPoint.y));
		
		
		// let the generator generate the params ?
		if(maxMonsterSpawn > 0){
			maxMonsterSpawn--;
			int[] power = new int[]{0, 0, 0, 0, 0};
			power[rnd.nextInt(power.length)] = 5;
			boolean spawnIsInRoom = GameControl.getControl().getMap().isWalkableRoom(spawnPoint.x, spawnPoint.y);
			EntityFactory.getFactory().makeMonster(spawnPoint.x, spawnPoint.y, spawnIsInRoom, 100, power, "test");
		}
	}

}
