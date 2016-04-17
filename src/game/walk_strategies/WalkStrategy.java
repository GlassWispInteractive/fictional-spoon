package game.walk_strategies;

import java.awt.Point;
import java.util.Random;

import game.control.GameControl;
import game.entities.Direction;
import game.entities.Entity;
import game.entities.Player;
import game.generator.Map;

public abstract class WalkStrategy {
    
    protected Map map;
    
    protected Random rnd = new Random();
    
    private boolean playerFarAway = true;
    private int area = 8;
    private boolean canBeAggro;
    
    private Point[] oldPoints = new Point[2];
    
    public WalkStrategy() {
        this.canBeAggro = rnd.nextBoolean();
        // this.canBeAggro = true;
    }
    
    public WalkStrategy(boolean canBeAggro) {
        this.canBeAggro = canBeAggro;
    }
    
    public Point walk(int oldX, int oldY) {
        
        if (oldPoints[1] == null) {
            oldPoints[0] = new Point(oldX, oldY);
            oldPoints[1] = new Point(oldX, oldY);
        }
        
        map = GameControl.getControl().getMap();
        
        Point newPoint = choseStrategy(oldX, oldY);
        
        if (!map.isWalkable(newPoint.x, oldY)) {
            newPoint.x = oldX;
        }
        if (!map.isWalkable(oldX, newPoint.y)) {
            newPoint.y = oldY;
        }
        
        if (map.isWalkable(newPoint.x, newPoint.y)) {
            // update last two points
            oldPoints[1].x = oldPoints[0].x;
            oldPoints[1].y = oldPoints[0].y;
            oldPoints[0].x = newPoint.x;
            oldPoints[0].y = newPoint.y;
            
            return newPoint;
        }
        
        return new Point(oldX, oldY);
    }
    
    private Point choseStrategy(int oldX, int oldY) {
        
        if (!canBeAggro) {
            return walkStrategy(oldX, oldY);
        }
        
        Entity player = Player.getNewest();
        int playerX = player.getX();
        int playerY = player.getY();
        playerFarAway = Math.abs(oldX - playerX) > area || Math.abs(oldY - playerY) > area;
        
        if (playerFarAway) {
            return walkStrategy(oldX, oldY);
        } else {
            return aggroWalk(oldX, oldY);
        }
    }
    
    protected Point randomWalk(int oldX, int oldY) {
        
        Point newPoint;
        
        do {
            
            // calc a random direction
            int directionIndex = rnd.nextInt(Direction.values().length);
            
            // get direction
            Direction dir = Direction.values()[directionIndex];
            
            // make move
            newPoint = dir.move(new Point(oldX, oldY));
            
        } while (!map.isWalkable(newPoint.x, newPoint.y));
        
        return newPoint;
        
    }
    
    protected Point aggroWalk(int oldX, int oldY) {
        
        Entity player = Player.getNewest();
        int playerX = player.getX();
        int playerY = player.getY();
        
        Point newPoint = new Point(oldX, oldY);
        
        Direction dir1 = null;
        Direction dir2 = null;
        
        if ((playerX - oldX) != 0) {
            
            if ((playerX - oldX) > 0) {
                // get direction
                dir1 = Direction.values()[1];
            } else {
                // get direction
                dir1 = Direction.values()[3];
            }
        }
        
        if ((playerY - oldY) != 0) {
            
            if ((playerY - oldY) > 0) {
                // get direction
                dir2 = Direction.values()[2];
            } else {
                // get direction
                dir2 = Direction.values()[0];
            }
        }
        
        // make move
        if (dir1 == null) {
            if (dir2 == null) {
                return new Point(oldX, oldY);
            }
            return dir2.move(newPoint);
        }
        return dir1.move(newPoint, dir2);
    }
    
    protected Point walkStrategy(int oldX, int oldY) {
        
        if (map.isWalkableRoom(oldX, oldY)) {
            return walkWithStrategy(oldX, oldY);
        }
        
        Point newPoint;
        
        int directionIndex = rnd.nextInt(Direction.values().length);
        
        for (int i = 0; i < 4; i++) {
            
            Direction dir = Direction.values()[directionIndex];
            newPoint = dir.move(new Point(oldX, oldY));
            
            if (map.isWalkable(newPoint.x, newPoint.y)) {
                if (newPoint.x != oldPoints[1].x || newPoint.y != oldPoints[1].y) {
                    return newPoint;
                }
            }
            
            directionIndex = (++directionIndex) % Direction.values().length;
            
        }
        
        newPoint = randomWalk(oldX, oldY);
        
        if (newPoint != null) {
            return newPoint;
        }
        
        System.err.println("No walkable field");
        
        return new Point(oldX, oldY);
    }
    
    protected abstract Point walkWithStrategy(int oldX, int oldY);
    
}
