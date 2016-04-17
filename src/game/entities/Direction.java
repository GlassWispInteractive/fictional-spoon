package game.entities;

import java.awt.Point;
import java.util.Random;

import game.control.GameControl;
import game.generator.Map;

public enum Direction {
    NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);
    
    private final int x;
    private final int y;
    private Random rnd = new Random();
    private Map map;
    
    private Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    // make move
    public Point move(Point oldPoint) {
        
        Point newPoint = new Point(oldPoint.x, oldPoint.y);
        
        newPoint.x += getX();
        newPoint.y += getY();
        
        return newPoint;
    }
    
    public Point move(Point oldPoint, Direction dir2) {
        
        map = GameControl.getControl().getMap();
        
        if (dir2 == null) {
            return move(oldPoint);
        }
        
        // try to walk both direction together
        Point newPoint = dir2.move(move(oldPoint));
        
        if (map.isWalkable(newPoint.x, newPoint.y)) {
            // both direction together is walkable
            return newPoint;
        }
        
        // calc both directions separately
        Point newPoint1 = move(oldPoint);
        Point newPoint2 = dir2.move(oldPoint);
        
        // try if one is not walkable
        if (!map.isWalkable(newPoint1.x, newPoint1.y)) {
            return newPoint2;
        }
        if (!map.isWalkable(newPoint2.x, newPoint2.y)) {
            return newPoint1;
        }
        
        // both are walkable and use one randomly
        if (rnd.nextBoolean()) {
            return newPoint1;
        } else {
            return newPoint2;
        }
    }
}
