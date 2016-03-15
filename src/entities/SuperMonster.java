package entities;

import game.ImageSource;
import game.TileFactory;
import game.TileSource;
import game.World;
import java.awt.Point;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;

public class SuperMonster extends Entity{
	
	private Random rnd = new Random();
	private TileFactory tileFac = TileFactory.getTilesFactory();
	private ImageSource imgSource = new ImageSource(TileSource.MONSTER_TILES, 4, 8);
	private int blocked = 0;
	private int maxMonsterSpawn = 3;

	public SuperMonster(int x, int y, String name) {
		super(x, y);
		
		delayTicks = 1000;
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

		do{
			
			dir = Direction.values()[directionIndex];
			
			spawnPoint = dir.move(new Point(x, y));
			
			directionIndex = (++directionIndex) % Direction.values().length;
			
		} while (!World.getWorld().getMap().isWalkable(spawnPoint.x, spawnPoint.y));
		
		
		// let the generator generate the params ?
		if(maxMonsterSpawn > 0){
			maxMonsterSpawn--;
			int[] power = new int[]{0, 0, 0, 0, 0};
			power[rnd.nextInt(power.length)] = 5;
			EntityFactory.getFactory().makeMonster(spawnPoint.x, spawnPoint.y, 100, power, "test");
		}
	}

}
