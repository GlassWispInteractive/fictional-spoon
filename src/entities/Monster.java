package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Monster extends Entity {
	private Paint[] color = { Paint.valueOf("RED"), Paint.valueOf("BLUE"), Paint.valueOf("WHITE"),
			Paint.valueOf("GRAY"), Paint.valueOf("PINK") };

	@SuppressWarnings("unused")
	private int hp;
	// earth, fire, air, water, mystic #korra
	private int[] power = new int[5];

	private int maxType = -1;
	private String name;
	private boolean monsterDead = false;

	public Monster(int x, int y, int hp, int[] power, String name) {
		super(x, y);

		this.hp = hp;
		this.name = name;

		this.power = power;

		maxType = -1;
		int max = -1;
		for (int i = 0; i < this.power.length; i++) {
			if (this.power[i] > max) {
				maxType = i;
				max = this.power[i];
			}
		}
	}
	
	public String getName(){
		return name;
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {

		if (monsterDead) {
			gc.setFill(Paint.valueOf("BLACK"));
			gc.fillOval((x - offsetX) * size + 1, (y - offsetY) * size + 1, size - 2, size - 2);
		} else {
			gc.setFill(Paint.valueOf("BLACK"));
			gc.fillRect((x - offsetX) * size + 1, (y - offsetY) * size + 1, size - 2, size - 2);
		}

		gc.setFill(color[maxType]);
		gc.fillRect((x - offsetX) * size + 4, (y - offsetY) * size + 4, size - 8, size - 8);

	}

	@Override
	public void tick(double elapsedTime) {
		// check intersection
		EntityFactory fac = EntityFactory.getFactory();
		if (x == fac.getPlayer().getX() && y == fac.getPlayer().getY()) {
			monsterDead = true;
		}
	}

}
