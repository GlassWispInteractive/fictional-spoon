package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Monster extends Entity {
	
	private int hp;
	private int mana;
	private int[] power = new int[5]; //fire, water, air, earth, mystic
	private Paint[] color = { Paint.valueOf("RED"), Paint.valueOf("BLUE"), Paint.valueOf("WHITE"),
			Paint.valueOf("GRAY"), Paint.valueOf("PINK")};
	private int maxType = -1;
	private String name;

	public Monster(int x, int y, int hp, int mp, int[]power, String name) {
		super(x, y);

		this.hp = hp;
		this.mana = mp;
		this.name = name;
		
		this.power = power;
		
		maxType= -1;
		int max = -1;
		for(int i = 0; i < this.power.length; i++){
			if(this.power[i] > max){
				maxType = i;
				max = this.power[i];
			}
		}
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {
		gc.setFill(Paint.valueOf("BLACK"));
		gc.fillRect((x - offsetX) * size + 1, (y - offsetY) * size + 1, size - 2, size - 2);
		
		gc.setFill(color[maxType]);
		gc.fillRect((x - offsetX) * size +4, (y - offsetY) * size +4, size - 8, size - 8);
	}

	@Override
	public void tick(double elapsedTime) {
		//check intersection
		EntityFactory fac = EntityFactory.getFactory();
		if(x == fac.getPlayer().getX() && y == fac.getPlayer().getY()){
			fac.deleteEntity(this);
		}

		
	}

}
