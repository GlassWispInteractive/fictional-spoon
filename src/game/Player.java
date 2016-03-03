package game;

import java.util.ArrayList;

import dungeon.Ground;
import dungeon.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Player {
	
	private int currentPositionX = 0;
	private int currentPositionY = 0;
	private int speed = 5;
	private Level level = Level.getLevel();
	
	private Image image;
	private double width = 5;
	private double height = 5;
	private int windowWidth = 1400;
	private int windowHeight = 900;
	
	public Player(int posX, int posY){
		this.currentPositionX = posX;
		this.currentPositionY = posY;
	}
	
	
	public void updatePlayer(ArrayList<String> input){
		
		int newPositionX = currentPositionX;
		int newPositionY = currentPositionY;

		if (input.contains("A")){
			newPositionX -=speed;
			if(newPositionX < 0){
				newPositionX = 0;
			}
		}
		if (input.contains("D")){
			newPositionX +=speed;
			if(newPositionX > windowWidth - width){
				newPositionX = (int) (windowWidth - width);
			}
		}
		if (input.contains("W")){
			newPositionY -=speed;
			if(newPositionY < 0){
				newPositionY = 0;
			}
		}
		if (input.contains("S")){
			newPositionY +=speed;
			if(newPositionY > windowHeight - height){
				newPositionY = (int) (windowHeight - height);
			}
		}
		
		Map map = level.getMap();
		Ground newGround = map.getGround(newPositionX/5, newPositionY/5);
		if(newGround == Ground.ROOM || newGround == Ground.FLOOR){
			currentPositionX = newPositionX;
			currentPositionY = newPositionY;
		}
	}
	
//	public void setImage(String filename) {
//		Image i = new Image(filename);
//		setImage(i);
//	}
//	public void setImage(Image i) {
//		image = i;
//		width = i.getWidth();
//		height = i.getHeight();
//	}
	
	public void render(GraphicsContext gc) {
		gc.setFill(Color.RED);
		gc.fillRect(currentPositionX, currentPositionY, width, height);
//		gc.drawImage(image, currentPositionX, currentPositionY);
	}

}
