package entities;

import com.sun.javafx.geom.Point2D;

import game.TileFactory;
import game.TileSource;
import javafx.scene.canvas.GraphicsContext;

public class Monster extends Entity {
	
	private Point2D[] tileType = {new Point2D(2,5), new Point2D(0,5), new Point2D(0,1), new Point2D(5,4), new Point2D(1,8)};

	@SuppressWarnings("unused")
	private int hp;
	// earth, fire, air, water, mystic #korra
	private int[] power = new int[5];

	private int maxType = -1;
	private String name;
	private boolean monsterDead = false;
	TileFactory tileFac;

	public Monster(int x, int y, int hp, int[] power, String name) {
		super(x, y);

		this.hp = hp;
		this.name = name;
		this.tileFac = TileFactory.getTilesFactory();
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
		
		int tileX;
		int tileY;
		
		if (monsterDead) {
			tileX = 0;
			tileY = 7;
		}else{
			tileX = (int) tileType[maxType].x;
			tileY = (int) tileType[maxType].y;
		}
		
		tileFac.drawTile(gc, TileSource.MONSTER_TILES, (x - offsetX), (y - offsetY), size, tileX, tileY);

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
