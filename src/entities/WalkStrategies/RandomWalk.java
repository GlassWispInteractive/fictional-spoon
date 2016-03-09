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

		Point newPoint = new Point(oldX, oldY);
		
		if(rnd.nextBoolean()){
			speedX *= -1;
		}
		if(rnd.nextBoolean()){
			speedY *= -1;
		}
		
		if(rnd.nextBoolean()){
			if(!map.isWalkable(newPoint.x + speedX, oldY)){
				speedX *= -1;
			}
			newPoint.x += speedX;
		} else {
			if(!map.isWalkable(oldX, newPoint.y + speedY)){
				speedY *= -1;
			}
			newPoint.y += speedY;
		}
		
		return newPoint;
		
	}

}
