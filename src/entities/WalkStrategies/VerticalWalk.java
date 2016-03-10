package entities.WalkStrategies;

import java.awt.Point;

public class VerticalWalk extends WalkStrategy {
	
	private int directionIndex = 0;
	
	public VerticalWalk() {
		super();
	}
	public VerticalWalk(boolean canBeAggro) {
		super(canBeAggro);
	}
	
	@Override
	public Point walkStrategy(int oldX, int oldY) {
		
		Point newPoint;
		
		// get direction
		Direction dir = Direction.values()[directionIndex];

		// make move
		newPoint = move(new Point(oldX, oldY),dir);
		
		if(!map.isWalkable(oldX, newPoint.y)){
			directionIndex = (directionIndex + 2) % Direction.values().length;
		}
		
		return newPoint;
		
	}
}
