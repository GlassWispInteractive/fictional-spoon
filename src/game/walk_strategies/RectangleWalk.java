package game.walk_strategies;

import java.awt.Point;

import game.entities.Direction;

public class RectangleWalk extends WalkStrategy {
	
	private int directionIndex = 0;
	
	public RectangleWalk() {
		super();
	}
	public RectangleWalk(boolean canBeAggro) {
		super(canBeAggro);
	}

	@Override
	protected Point walkWithStrategy(int oldX, int oldY) {
		
		Point newPoint;
		
		// get direction
		Direction dir = Direction.values()[directionIndex];

		// make move
		newPoint = dir.move(new Point(oldX, oldY));
		
		
		if(!map.isWalkable(newPoint.x, newPoint.y) || !map.isWalkableRoom(newPoint.x, newPoint.y)){
			
			directionIndex = (++directionIndex) % Direction.values().length;
			
			return new Point(oldX, oldY);
		}
		
		return newPoint;
	}

}
