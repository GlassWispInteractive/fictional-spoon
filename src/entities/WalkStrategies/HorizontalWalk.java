package entities.WalkStrategies;

import java.awt.Point;

public class HorizontalWalk extends WalkStrategy{
	
	private int directionIndex = 1;
	
	public HorizontalWalk() {
		super();
		
	}

	@Override
	public Point walkStrategy(int oldX, int oldY) {
		
		Point newPoint;
		
		// get direction
		Direction dir = Direction.values()[directionIndex];

		// make move
		newPoint = move(new Point(oldX, oldY),dir);
		
		if(!map.isWalkable(newPoint.x, oldY)){
			directionIndex = (directionIndex + 2) % Direction.values().length;
		}
		
		return newPoint;
		
	}
}
