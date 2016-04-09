package game.entities;

import java.awt.Point;
import java.util.Random;

import engine.TileMaster;
import framework.GameControl;
import game.combat.Quest;
import javafx.scene.canvas.GraphicsContext;

public class Portal extends Entity {
    private static int count = 0;
    
    private Random rnd = new Random();
    private TileMaster tileFac = TileMaster.getInstance();
    // private ImageSource imgSource = new ImageSource(TileSource.MAP_TILES, 23,
    // 7); //alternative
    // private ImageSource imgSource = new ImageSource(TileSource.MAP_TILES, 36,
    // 1); //alternative
    private int blocked = 0;
    private int maxMonsterSpawn = 3;
    private boolean destroyed = false;
    
    public Portal(int x, int y) {
        super(x, y);
        
        delayTicks = 1000;
        
        count += 1;
    }
    
    @Override
    public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {
        
        if (destroyed) {
            gc.drawImage(tileFac.getTile("map", 44, 9), size *(x - offsetX), size *(y - offsetY));
        } else {
            gc.drawImage(tileFac.getTile("map", 40, 9), size *(x - offsetX), size *(y - offsetY));
        }
    }
    
    @Override
    public void tick(int ticks) {
        if (blocked >= 0) {
            blocked -= ticks;
        }
        
        if (blocked < 0 && !destroyed) {
            blocked = delayTicks;
            
            spawnMonster();
        }
        
        if (intersectsWithPlayer() && !destroyed) {
            destroyed = true;
            
            // alert
            GameControl.getControl().alert("YKWTBI");
            
            // goal update
            GameControl.getControl().updateGoal(Quest.Goal.PORTAL);
        }
    }
    
    protected static void resetCount() {
        count = 0;
    }
    
    private void spawnMonster() {
        
        int directionIndex = rnd.nextInt(Direction.values().length);
        
        Direction dir;
        Point spawnPoint;
        
        do {
            dir = Direction.values()[directionIndex];
            
            spawnPoint = dir.move(new Point(x, y));
            
            directionIndex = (++directionIndex) % Direction.values().length;
            
        } while (!GameControl.getControl().getMap().isWalkable(spawnPoint.x, spawnPoint.y));
        
        // let the generator generate the params ?
        if (maxMonsterSpawn > 0) {
            // generate monster parameter
            // MonsterType type =
            // MonsterType.values()[rnd.nextInt(MonsterType.values().length -
            // 1)];
            // int dmg = rnd.nextInt(3) + 1;
            
            // make monster at (x, y)
            boolean spawnIsInRoom = GameControl.getControl().getMap().isWalkableRoom(x, y);
            new Monster(x, y, spawnIsInRoom);
        }
    }
    
    public static int getCount() {
        return count;
    }
    
}
