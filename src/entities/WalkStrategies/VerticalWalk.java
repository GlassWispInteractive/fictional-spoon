package entities.WalkStrategies;

import java.awt.Point;

public class VerticalWalk extends WalkStrategy {
	
	@Override
	public Point walkStrategy(int oldX, int oldY) {
		
		Point newPoint = new Point(oldX, oldY);
		
		newPoint.y +=speedY;
		
		if(!map.isWalkable(oldX, newPoint.y)){
			speedY *= -1;
		}

		return newPoint;
	}

}
