package game.walk_strategies;

import java.awt.Point;

import game.entities.Direction;

public class HorizontalWalk extends WalkStrategy {
    
    private int directionIndex = 1;
    
    public HorizontalWalk() {
        super();
    }
    
    public HorizontalWalk(boolean canBeAggro) {
        super(canBeAggro);
    }
    
    @Override
    public Point walkWithStrategy(int oldX, int oldY) {
        
        Point newPoint;
        
        // get direction
        Direction dir = Direction.values()[directionIndex];
        
        // make move
        newPoint = dir.move(new Point(oldX, oldY));
        
        if (!map.isWalkableRoom(newPoint.x, oldY)) {
            directionIndex = (directionIndex + 2) % Direction.values().length;
        }
        
        return newPoint;
        
    }
}
