package game.walk_strategies;

import java.awt.Point;

import game.entities.Direction;
import game.generator.Ground;

public class FloorWalk extends WalkStrategy {
    
    public FloorWalk() {
        super();
    }
    
    public FloorWalk(boolean canBeAggro) {
        super(canBeAggro);
    }
    
    @Override
    protected Point walkWithStrategy(int oldX, int oldY) {
        
        // check if floor is next by
        Point newPoint;
        int directionIndex = rnd.nextInt(Direction.values().length);
        
        for (int i = 0; i < 4; i++) {
            
            Direction dir = Direction.values()[directionIndex];
            newPoint = dir.move(new Point(oldX, oldY));
            
            if (map.getGround(newPoint.x, newPoint.y) == Ground.FLOOR) {
                return newPoint;
            }
            
            directionIndex = (++directionIndex) % Direction.values().length;
            
        }
        
        return randomWalk(oldX, oldY);
        
    }
}
