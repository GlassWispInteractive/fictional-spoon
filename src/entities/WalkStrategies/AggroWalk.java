package entities.WalkStrategies;

import java.awt.Point;
import entities.Entity;
import entities.EntityFactory;

public class AggroWalk extends WalkStrategy{
	
	private RandomWalk rndWalk;
	private int area = 5;
	private boolean playerFarAway = true;;
	
	public AggroWalk() {
		super();
		
		rndWalk = new RandomWalk();
	}

	@Override
	protected Point walkStrategy(int oldX, int oldY) {
		
		Entity player = EntityFactory.getFactory().getPlayer();
		
		int playerX = player.getX();
		int playerY = player.getY();
		
		playerFarAway = Math.abs(oldX - playerX) > area || Math.abs(oldY - playerY) > area;
		
		if(playerFarAway){
			return rndWalk.walk(oldX, oldY);
		}
		//else - go to player
		
		Point newPoint = new Point(oldX, oldY);
		
		speedX = playerX - oldX;
		speedY = playerY - oldY;
		
		if((playerX - oldX) != 0){
			speedX /= Math.abs(speedX);
		}
		if((playerY - oldY) != 0){
			speedY /= Math.abs(speedY);
		}
		
		newPoint.x += speedX;
		newPoint.y += speedY;
		
		return newPoint;
		
	}

}
