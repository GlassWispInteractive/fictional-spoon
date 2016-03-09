package entities.WalkStrategies;

import game.World;
import gen.environment.Map;

import java.awt.Point;


public abstract class WalkStrategy {
	
	protected int blocked = 0;
	protected int speedX = 1;
	protected int speedY = 1;
	protected int delayTicks = 20;
	protected Map map;
	
	public Point walk(int oldX, int oldY){
		
		map = World.getWorld().getMap();

		Point newPoint = walkStrategy(oldX, oldY);
		
		if(blocked > 0){
			blocked--;
		}
		
		if(blocked <= 0){
			
			blocked = delayTicks - 1;
			
			if(!map.isWalkable(newPoint.x, oldY)){
				newPoint.x = oldX;
			}
			if(!map.isWalkable(oldX, newPoint.y)){
				newPoint.y = oldY;
			}

			return newPoint;
		}
		
		return new Point(oldX, oldY);
	}
	
	protected abstract Point walkStrategy(int oldX, int oldY);

}
