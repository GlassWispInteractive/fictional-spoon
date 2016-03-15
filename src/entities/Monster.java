package entities;

import framework.ImageSource;
import framework.TileFactory;
import framework.TileSource;
import javafx.scene.canvas.GraphicsContext;

public class Monster extends Entity {

	private ImageSource[] tileType = { new ImageSource(TileSource.MONSTER_TILES, 2, 5),
			new ImageSource(TileSource.MONSTER_TILES, 0, 5), new ImageSource(TileSource.MONSTER_TILES, 0, 1),
			new ImageSource(TileSource.MONSTER_TILES, 5, 4), new ImageSource(TileSource.MONSTER_TILES, 1, 8),
			new ImageSource(TileSource.MONSTER_TILES, 0, 7) };

	// 2,5), new Point2D(0,5), new Point2D(0,1), new Point2D(5,4), new
	// Point2D(1,8)};

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

	public String getName() {
		return name;
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {

		if (monsterDead) {
			tileFac.drawTile(gc, tileType[tileType.length - 1], (x - offsetX), (y - offsetY), size);
		} else {
			tileFac.drawTile(gc, tileType[maxType], (x - offsetX), (y - offsetY), size);
		}

	}

	@Override
	public void tick(double elapsedTime) {
		// check intersection
		EntityFactory fac = EntityFactory.getFactory();
		if (x == fac.getPlayer().getX() && y == fac.getPlayer().getY()) {
			
		}
	}

	public ImageSource getImageSource() {
		return tileType[maxType];
	}

}
