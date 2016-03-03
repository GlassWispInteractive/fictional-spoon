package game;


import dungeon.Ground;
import dungeon.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Player {
	private Events e = Events.getEvents();
	
	private int currentPositionX = 0;
	private int currentPositionY = 0;
	private Level level = Level.getLevel();
	
	private Image image;
	private int fieldSizeMult = 5;
	private int windowWidth = 1400;
	private int windowHeight = 900;
	
	public Player(int posX, int posY){
		this.currentPositionX = posX;
		this.currentPositionY = posY;
	}
	
	
	public void tick(){
		
		int newPositionX = currentPositionX;
		int newPositionY = currentPositionY;

		if (e.isLeft()){
			newPositionX --;
			if(newPositionX < 0){
				newPositionX = 0;
			}
		}
		if (e.isRight()){
			newPositionX ++;
			if(newPositionX > windowWidth - fieldSizeMult){
				newPositionX = (int) (windowWidth - fieldSizeMult);
			}
		}
		if (e.isUp()){
			newPositionY --;
			if(newPositionY < 0){
				newPositionY = 0;
			}
		}
		if (e.isDown()){
			newPositionY ++;
			if(newPositionY > windowHeight - fieldSizeMult){
				newPositionY = (int) (windowHeight - fieldSizeMult);
			}
		}
		
		Map map = level.getMap();
		Ground newGround = map.getGround(newPositionX, newPositionY);
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
		gc.fillRect(fieldSizeMult*currentPositionX, fieldSizeMult*currentPositionY, fieldSizeMult, fieldSizeMult);
//		gc.drawImage(image, currentPositionX, currentPositionY);
	}

}
