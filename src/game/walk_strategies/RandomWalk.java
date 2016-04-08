package game.walk_strategies;

import java.awt.Point;

public class RandomWalk extends WalkStrategy {

	public RandomWalk() {
		super();
	}

	public RandomWalk(boolean canBeAggro) {
		super(canBeAggro);
	}

	@Override
	protected Point walkWithStrategy(int oldX, int oldY) {

		return randomWalk(oldX, oldY);

	}
}
