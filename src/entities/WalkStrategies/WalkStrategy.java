package entities.WalkStrategies;

import game.World;
import gen.environment.Map;

import java.awt.Point;


public abstract class WalkStrategy {
	
	protected int blocked = 0;
	protected int speedX = 1;
	protected int speedY = 1;
	protected int delayTicks = 10;
	protected Map map;
	
	public Point walk(int oldX, int oldY){
		
		map = World.getWorld().getMap();

		Point newPoint = walkStrategy(oldX, oldY);
		
		if(blocked > 0){
			blocked--;
		}
		
		if(map.isWalkable(newPoint.x, newPoint.y) && blocked <= 0){
			
			System.out.println("is walkable");
			
			blocked = delayTicks - 1;

			return newPoint;
		}else{
			System.out.println("is not walkable");
		}
		
		return new Point(oldX, oldY);
	}
	
	protected abstract Point walkStrategy(int oldX, int oldY);

}
