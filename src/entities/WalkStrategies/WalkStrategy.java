package entities.WalkStrategies;

import game.World;
import gen.environment.Map;

import java.awt.Point;


public abstract class WalkStrategy {
	
	private int blocked = 0;
	protected int speedX = 1;
	protected int speedY = 1;
	private int delayTicks = 20;
	protected Map map;
	
	protected enum Direction {
	    NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);

	    private final int x;
	    private final int y;

	    private Direction(int x, int y) {
	        this.x = x;
	        this.y = y;
	    }
	    public int getX() { return x; }
	    public int getY() { return y; }
	}
	
	// make move
	protected Point move(Point oldPoint, Direction direction) {
		
		Point newPoint = new Point(oldPoint.x, oldPoint.y);
		
		newPoint.x += direction.getX();
		newPoint.y += direction.getY();
		
		return newPoint;
	}
	
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
