package entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import combat.Combat;

import engine.ImageSource;
import engine.TileFactory;
import engine.TileSource;
import entities.WalkStrategies.HorizontalWalk;
import entities.WalkStrategies.RandomWalk;
import entities.WalkStrategies.RectangleWalk;
import entities.WalkStrategies.VerticalWalk;
import entities.WalkStrategies.WalkStrategy;
import javafx.scene.canvas.GraphicsContext;

public class Opponent extends Entity {
	
	private TileFactory tileFac = TileFactory.getTilesFactory();
	private ImageSource imgSource = new ImageSource(TileSource.CHAR_TILES, 0, 11);
	private int blocked = 0;
	Random rnd = new Random();
	
	private ArrayList<WalkStrategy> walkStrategies = new ArrayList<WalkStrategy>(Arrays.asList(
			new WalkStrategy[] {new RandomWalk(), new HorizontalWalk(), new VerticalWalk(), new RectangleWalk()}));
	private WalkStrategy currentWalkStrategy;
	
	private ArrayList<Monster> monsterList = new ArrayList<Monster>();

	public Opponent(int x, int y, String name) {
		super(x, y);
		
		this.delayTicks = 10;
		int chosenStrategy = new Random().nextInt(walkStrategies.size());
		this.currentWalkStrategy = walkStrategies.get(chosenStrategy);
		
		//hard coded monster
		for(int i = 0; i < 3; i++){
			int[] power = new int[]{0, 0, 0, 0, 0};
			power[rnd.nextInt(power.length)] = 5;
			monsterList.add(new Monster(x, y, 100, power, "myMonster"));
		}
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {
		tileFac.drawTile(gc, imgSource, (x - offsetX), (y - offsetY), size);
	}

	@Override
	public void tick(double elapsedTime) {
		
		if(blocked >= 0){
			blocked--;
		}
		
		if(blocked < 0){
			blocked = delayTicks - 1;
			
			Point newPosition = currentWalkStrategy.walk(x, y);

			x = newPosition.x;
			y = newPosition.y;
		}
		
		EntityFactory fac = EntityFactory.getFactory();
		if (x == fac.getPlayer().getX() && y == fac.getPlayer().getY()) {
			new Combat(monsterList).start();
		}
		
	}
	
	public ArrayList<Monster> getMonsterList(){
		return monsterList;
	}

}
