package entities.WalkStrategies;

import java.awt.Point;

public class RandomWalk extends WalkStrategy{
	
	public RandomWalk() {
		super();
	}
	public RandomWalk(boolean canBeAggro) {
		super(canBeAggro);
	}
	


	@Override
	protected Point walkStrategy(int oldX, int oldY) {
		
		return randomWalk(oldX, oldY);
		
	}
}
