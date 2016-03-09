package entities.WalkStrategies;

import java.awt.Point;

public class HorizontalWalk extends WalkStrategy{
	
	public HorizontalWalk() {
		super();
		
	}

	@Override
	public Point walkStrategy(int oldX, int oldY) {
		
		Point newPoint = new Point(oldX, oldY);
		
		newPoint.x +=speedX;
		
		if(!map.isWalkable(newPoint.x, oldY)){
			speedX *= -1;
		}

		return newPoint;
	}

}
