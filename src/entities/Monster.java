package entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import entities.WalkStrategies.FloorWalk;
import entities.WalkStrategies.HorizontalWalk;
import entities.WalkStrategies.RandomWalk;
import entities.WalkStrategies.RectangleWalk;
import entities.WalkStrategies.VerticalWalk;
import entities.WalkStrategies.WalkStrategy;
import framework.ScreenControl;
import combat.Attacks;
import combat.Combat;
import combat.Combo;
import combat.IAttackable;
import engine.ImageSource;
import engine.TileFactory;
import engine.TileSource;
import javafx.scene.canvas.GraphicsContext;

public class Monster extends Entity implements IAttackable {

	private ImageSource[] tileType = { new ImageSource(TileSource.MONSTER_TILES, 2, 5),
			new ImageSource(TileSource.MONSTER_TILES, 0, 5), new ImageSource(TileSource.MONSTER_TILES, 0, 1),
			new ImageSource(TileSource.MONSTER_TILES, 5, 4), new ImageSource(TileSource.MONSTER_TILES, 1, 8),
			new ImageSource(TileSource.MONSTER_TILES, 0, 7) };
	
	public enum MonsterType {
		EARTH, FIRE, AIR, WATER, MYSTIC
	};

	// 2,5), new Point2D(0,5), new Point2D(0,1), new Point2D(5,4), new
	// Point2D(1,8)};

	private ArrayList<WalkStrategy> walkStrategiesInRoom = new ArrayList<WalkStrategy>(Arrays.asList(new WalkStrategy[] {
			new RandomWalk(), new HorizontalWalk(), new VerticalWalk(), new RectangleWalk() }));
	private WalkStrategy currentWalkStrategy;
	
	// for speed
	private int blocked = 0;

	private int hp;
	private final int maxHp;
	
	MonsterType type;
	int dmg;
	// earth, fire, air, water, mystic #korra
//	private int[] power = new int[5];
	private Attacks attack;
//
//	private int maxType = -1;
	private String name;
	private boolean monsterDead = false;
	private TileFactory tileFac = TileFactory.getTilesFactory();

	public Monster(int x, int y, boolean spawnIsInRoom, int hp, MonsterType type, int dmg, String name) {
		super(x, y);

		this.hp = hp;
		this.maxHp = hp;
		this.name = name;
		this.delayTicks = 20;
		
		this.type = type;
		this.dmg = dmg;
		this.attack = new Attacks(dmg);

		if(spawnIsInRoom) {
			int chosenStrategy = new Random().nextInt(walkStrategiesInRoom.size());
			this.currentWalkStrategy = walkStrategiesInRoom.get(chosenStrategy);
			// this.currentWalkStrategy = new RandomWalk();
			// this.currentWalkStrategy = new HorizontalWalk();
			// this.currentWalkStrategy = new VerticalWalk();
			// this.currentWalkStrategy = new RectangleWalk();
		} else {
		    	this.currentWalkStrategy = new FloorWalk();
		}

		
//		maxType = -1;
//		int max = -1;
//		for (int i = 0; i < this.power.length; i++) {
//			if (this.power[i] > max) {
//				maxType = i;
//				max = this.power[i];
//			}
//		}
		
	}

	public String getName() {
		return name;
	}

	public boolean isDead() {
		return monsterDead;
	}

	public String getHpInfo() {
		return "[" + hp + " / " + maxHp + "]";
	}

	@Override
	public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {

		if (monsterDead) {
			tileFac.drawTile(gc, tileType[tileType.length - 1], (x - offsetX), (y - offsetY), size);
		} else {
			tileFac.drawTile(gc, tileType[type.ordinal()], (x - offsetX), (y - offsetY), size);
		}
	}

	@Override
	public void tick(int ticks) {
	    
	    	if(monsterDead) {
			return;
	    	}
	    	
	    	//check intersection before AND after moving
	    	// check intersection
	    	if (intersectsWithPlayer()) {
	    		startCombat();
	    	}
	    
		if (blocked >= 0) {
			blocked -= ticks;
		}
		
		if (!monsterDead) {
		    	
		    	if(blocked <= 0) {
        		    	blocked += delayTicks;
        
        			Point newPosition = currentWalkStrategy.walk(x, y);
        
        			x = newPosition.x;
        			y = newPosition.y;
		    	}

			// check intersection
			if (intersectsWithPlayer()) {
			    	startCombat();
			}
		}

	}
	
	private void startCombat() {
		// goal update
		// moved to Combat class - stays here for debugging
//		 GameControl.getControl().updateGoal(Goal.MONSTER);

		// game logic
//		monsterDead = true; // debug
		ScreenControl.getCtrl().addScreen("combat", new Combat(new Monster[] { this }));
		ScreenControl.getCtrl().setScreen("combat");
	}

	public ImageSource getImageSource() {
		return tileType[type.ordinal()];
	}

	@Override
	public void getDmg(int dmg) {
		hp -= dmg;

		if (hp <= 0) {
			monsterDead = true;
		}
	}

	@Override
	public void doAttack(IAttackable focus) {
		attack.doAttack(focus);
	}

	@Override
	public void doAttack(IAttackable focus, Combo combo) {
		attack.doAttack(focus, combo);
	}

}
