package entities.WalkStrategies;

import entities.Entity;
import entities.EntityFactory;
import game.World;
import gen.environment.Map;

import java.awt.Point;
import java.util.Random;


public abstract class WalkStrategy {
	
	protected Map map;
	
	private Random rnd = new Random();
	private int blocked = 0;
	private int delayTicks = 20;
	
	private boolean playerFarAway = true;
	private int area = 8;
	private boolean aggro = false;
	private boolean canBeAggro;
	
	public WalkStrategy() {
		this.canBeAggro = rnd.nextBoolean();
//		this.canBeAggro = true;
	}
	public WalkStrategy(boolean canBeAggro) {
		this.canBeAggro = canBeAggro;
	}
	
	protected enum Direction {
	    NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);

	    private final int x;
	    private final int y;

	    private Direction(int x, int y) {
	        this.x = x;
	        this.y = y;
	    }
	    public int getX() { return x; }
	    public int getY() { return y; }
	}
	
	// make move
	protected Point move(Point oldPoint, Direction dir) {
		
		if(dir == null){
			return oldPoint;
		}
		
		Point newPoint = new Point(oldPoint.x, oldPoint.y);
		
		newPoint.x += dir.getX();
		newPoint.y += dir.getY();
		
		return newPoint;
	}
	protected Point move(Point oldPoint, Direction dir1, Direction dir2) {
		
		if(dir1 == null){
			move(oldPoint, dir2);
		}
		if(dir2 == null){
			move(oldPoint, dir1);
		}
		
		//try to walk both direction together
		Point newPoint = move(move(oldPoint, dir1), dir2);
		
		if(map.isWalkable(newPoint.x, newPoint.y)){
			//both direction together is walkable
			return newPoint;
		}
		
		//calc both directions separately
		Point newPoint1 = move(oldPoint, dir1);
		Point newPoint2 = move(oldPoint, dir2);
		
		//try if one is not walkable
		if(!map.isWalkable(newPoint1.x, newPoint1.y)){
			return newPoint2;
		}
		if(!map.isWalkable(newPoint2.x, newPoint2.y)){
			return newPoint1;
		}
		
		//both are walkable and use one randomly
		if(rnd.nextBoolean()){
			return newPoint1;
		}else{
			return newPoint2;
		}
	}
	
	public Point walk(int oldX, int oldY){
		
		map = World.getWorld().getMap();

		Point newPoint = choseStrategy(oldX, oldY);
		
		if(blocked > 0){
			blocked--;
		}
		
		if(blocked <= 0){
			
			blocked = delayTicks - 1;
			
			if(!map.isWalkable(newPoint.x, oldY)){
				newPoint.x = oldX;
			}
			if(!map.isWalkable(oldX, newPoint.y)){
				newPoint.y = oldY;
			}

			if(map.isWalkable(newPoint.x, newPoint.y)){
				return newPoint;
			}
		}
		
		return new Point(oldX, oldY);
	}
	
	private Point choseStrategy(int oldX, int oldY) {
		
		if(!canBeAggro){
			return walkStrategy(oldX, oldY);
		}
		
		Entity player = EntityFactory.getFactory().getPlayer();
		int playerX = player.getX();
		int playerY = player.getY();
		playerFarAway = Math.abs(oldX - playerX) > area || Math.abs(oldY - playerY) > area;
		
		if(!playerFarAway){
			aggro = true;
		}		
		if(playerFarAway && map.isWalkableRoom(oldX, oldY)){
			aggro = false;
		}
		
		Point newPoint;
		
		if(!aggro){
			newPoint = walkStrategy(oldX, oldY);
		} else {
			
			if(playerFarAway){
				newPoint = randomWalk(oldX, oldY);
			} else {
				newPoint = aggroWalk(oldX, oldY);
			}
		}
		
		return newPoint;
	}
	
	protected Point randomWalk(int oldX, int oldY) {
		
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
	
	protected Point aggroWalk(int oldX, int oldY) {
		
		Entity player = EntityFactory.getFactory().getPlayer();
		int playerX = player.getX();
		int playerY = player.getY();
		
		Point newPoint = new Point(oldX, oldY);
		
		Direction dir1 = null;
		Direction dir2 = null;
		
		if((playerX - oldX) != 0){
			
			if((playerX - oldX) > 0) {
				// get direction
				dir1 = Direction.values()[1];
			} else {
				// get direction
				dir1 = Direction.values()[3];
			}
		}
		
		if((playerY - oldY) != 0){
			
			if((playerY - oldY) > 0) {
				// get direction
				dir2 = Direction.values()[2];
			} else {
				// get direction
				dir2 = Direction.values()[0];
			}
		}
		
		// make move
		newPoint = move(newPoint, dir1, dir2);
		
		return newPoint;
	}
	
	
	protected abstract Point walkStrategy(int oldX, int oldY);

}
