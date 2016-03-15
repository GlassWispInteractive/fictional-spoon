package entities;

import game.ImageSource;
import game.TileFactory;
import game.TileSource;
import game.World;
import gen.environment.Map;

import java.awt.Point;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;

public class SuperMonster extends Entity{
	
	private Random rnd = new Random();
	private TileFactory tileFac = TileFactory.getTilesFactory();
	private ImageSource imgSource = new ImageSource(TileSource.MONSTER_TILES, 4, 8);
	private int blocked = 0;

	public SuperMonster(int x, int y, String name) {
		super(x, y);
		
		delayTicks = 10000;
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {

		tileFac.drawTile(gc, imgSource, (x - offsetX), (y - offsetY), size);
	}

	@Override
	public void tick(double elapsedTime) {
		
		if (blocked >= 0) {
			blocked--;
		}
		
		if (blocked < 0) {
			
			blocked = delayTicks - 1;
			
			spawnMonster();
			
		}
		
	}
	
	private void spawnMonster() {
		
		int directionIndex = rnd.nextInt(Direction.values().length);
	
		Direction dir;
		Point spawnPoint;
		
		dir = Direction.values()[directionIndex];
		
		spawnPoint = dir.move(new Point(x, y));
		
//		System.out.println("############");
//		System.out.println("im here: " + new Point(x, y));
		
//		System.out.println(directionIndex);
		if(World.getWorld().getMap().isWalkable(spawnPoint.x, spawnPoint.y)){
//			System.out.println("found spawnPoint "+spawnPoint);
			return;
		}
		directionIndex = (++directionIndex) % Direction.values().length;
//		System.out.println(directionIndex);
		if(World.getWorld().getMap().isWalkable(spawnPoint.x, spawnPoint.y)){
//			System.out.println("found spawnPoint "+spawnPoint);
			return;
		}
		directionIndex = (++directionIndex) % Direction.values().length;
//		System.out.println(directionIndex);
		if(World.getWorld().getMap().isWalkable(spawnPoint.x, spawnPoint.y)){
//			System.out.println("found spawnPoint "+spawnPoint);
			return;
		}
		directionIndex = (++directionIndex) % Direction.values().length;
//		System.out.println(directionIndex);
		if(World.getWorld().getMap().isWalkable(spawnPoint.x, spawnPoint.y)){
//			System.out.println("found spawnPoint "+spawnPoint);
			return;
		}
		
//		System.out.println(directionIndex);
		System.out.println("############");
		System.out.println("im here: " + new Point(x, y));
		System.out.println("no spawnpoint");
		return;

//		do{
//			
//			dir = Direction.values()[directionIndex];
//			
//			spawnPoint = dir.move(new Point(x, y));
//			
//			directionIndex = (++directionIndex) % Direction.values().length;
//			
//		} while (World.getWorld().getMap().isWalkable(spawnPoint.x, spawnPoint.y));
		
		
		// let the generator generate the params ?
//		EntityFactory.getFactory().makeMonster(spawnPoint.x, spawnPoint.y, 100, new int[]{1, 2, 3, 4, 5}, "test");
	}

}
