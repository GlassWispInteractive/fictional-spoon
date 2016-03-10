package entities.WalkStrategies;

import java.awt.Point;

public class RectangleWalk extends WalkStrategy {
	
	private int directionIndex = 0;
	
	public RectangleWalk() {
		super();

	}

	@Override
	protected Point walkStrategy(int oldX, int oldY) {
		
		Point newPoint;
		
		// get direction
		Direction dir = Direction.values()[directionIndex];

		// make move
		newPoint = move(new Point(oldX, oldY),dir);
		
		
		if(!map.isWalkable(newPoint.x, newPoint.y) || !map.isWalkableRoom(newPoint.x, newPoint.y)){
			
			directionIndex = (++directionIndex) % Direction.values().length;
			
			return new Point(oldX, oldY);
		}
		
		return newPoint;
	}

}
