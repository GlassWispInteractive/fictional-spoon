package entities.WalkStrategies;

import java.awt.Point;
import java.util.Random;

public class RandomWalk extends WalkStrategy{
	
	Random rnd;
	
	public RandomWalk() {
		super();
		
		rnd = new Random();
	}
	


	@Override
	protected Point walkStrategy(int oldX, int oldY) {
		
		Point newPoint;
		
		do {
			
			//  calc a random direction
			int directionIndex = rnd.nextInt(Direction.values().length);
	
			// get direction
			Direction dir = Direction.values()[directionIndex];
	
			// make move
			newPoint = move(new Point(oldX, oldY),dir);
			
		} while (!map.isWalkable(newPoint.x, newPoint.y));

		
		return newPoint;
		
	}
}
