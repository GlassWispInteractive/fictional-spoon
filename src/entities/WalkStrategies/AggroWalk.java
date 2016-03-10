package entities.WalkStrategies;

import java.awt.Point;
import java.util.Random;

import entities.Entity;
import entities.EntityFactory;

public class AggroWalk extends WalkStrategy{
	
	private int area = 8;
	private boolean playerFarAway = true;
	private Random rnd;
	
	public AggroWalk() {
		super();
		
		rnd = new Random();
	}

	@Override
	protected Point walkStrategy(int oldX, int oldY) {
		
		Entity player = EntityFactory.getFactory().getPlayer();
		
		int playerX = player.getX();
		int playerY = player.getY();
		
		playerFarAway = Math.abs(oldX - playerX) > area || Math.abs(oldY - playerY) > area;
		
		Point newPoint = new Point(oldX, oldY);
		
		if(playerFarAway){
		//random walking
			do {
				//  calc a random direction
				int directionIndex = rnd.nextInt(Direction.values().length);
		
				// get direction
				Direction dir = Direction.values()[directionIndex];
		
				// make move
				newPoint = move(new Point(oldX, oldY),dir);
				
			} while (!map.isWalkable(newPoint.x, newPoint.y));
		}else{
		//else - go to player
			
			if((playerX - oldX) != 0){
				
				Direction dir;
				
				if((playerX - oldX) > 0) {
					// get direction
					dir = Direction.values()[1];
				} else {
					// get direction
					dir = Direction.values()[3];
				}

				// make move
				newPoint = move(newPoint, dir);
			}
			
			if((playerY - oldY) != 0){
				
				Direction dir;
				
				if((playerY - oldY) > 0) {
					// get direction
					dir = Direction.values()[2];
				} else {
					// get direction
					dir = Direction.values()[0];
				}

				// make move
				newPoint = move(newPoint, dir);
			}
		
		}
		
		return newPoint;
		
	}
}
