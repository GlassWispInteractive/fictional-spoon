package entities.WalkStrategies;

import entities.Direction;
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
			newPoint = dir.move(new Point(oldX, oldY));
			
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
		if(dir1 == null){
			return dir2.move(newPoint);
		}
		return dir1.move(newPoint, dir2);
	}
	
	
	protected abstract Point walkStrategy(int oldX, int oldY);

}
