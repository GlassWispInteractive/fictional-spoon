package game.entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import engine.TileFactory;
import engine.TileSource;
import framework.ScreenControl;
import game.walk_strategies.FloorWalk;
import game.walk_strategies.HorizontalWalk;
import game.walk_strategies.RandomWalk;
import game.walk_strategies.RectangleWalk;
import game.walk_strategies.VerticalWalk;
import game.walk_strategies.WalkStrategy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import screens.CombatScreen;

public class Opponent extends Entity {
    private TileFactory tileFac = TileFactory.getTilesFactory();
    private int blocked = 0;
    private boolean opponentDead = false;
    Random rnd = new Random();
    
    private ArrayList<WalkStrategy> walkStrategies = new ArrayList<WalkStrategy>(Arrays.asList(new WalkStrategy[] {
            new RandomWalk(), new HorizontalWalk(), new VerticalWalk(), new FloorWalk(), new RectangleWalk() }));
    private WalkStrategy currentWalkStrategy;
    
    private ArrayList<Monster> monsterList = new ArrayList<>();
    
    public Opponent(int x, int y, boolean spawnIsInRoom) {
        super(x, y);
        
        this.delayTicks = 10;
        int chosenStrategy = new Random().nextInt(walkStrategies.size());
        this.currentWalkStrategy = walkStrategies.get(chosenStrategy);
        
        // hard coded monster
        for (int i = 0; i < 3; i++) {
            // generate monster parameter
            // MonsterType type =
            // MonsterType.values()[rnd.nextInt(MonsterType.values().length -
            // 1)];
            // int dmg = rnd.nextInt(3) + 1;
            
            // make monster at (x, y)
            new Monster(x, y, spawnIsInRoom);
        }
    }
    
    /**
     * function generates a monster at a specified place
     * 
     * @param x
     * @param y
     * @param spawnIsInRoom
     */
    public static void generate(int x, int y, boolean spawnIsInRoom) {
        new Opponent(x, y, spawnIsInRoom);
    }
    
    @Override
    public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {
        
        if (opponentDead) {
            gc.setFill(Color.RED);
            gc.fillRect((x - offsetX) * size, (y - offsetY) * size, size, size);
            tileFac.drawTile(gc, TileSource.CHAR_TILES, 0, 11, (x - offsetX), (y - offsetY), size);
        } else {
            tileFac.drawTile(gc, TileSource.CHAR_TILES, 0, 11, (x - offsetX), (y - offsetY), size);
        }
    }
    
    @Override
    public void tick(int ticks) {
        
        if (!opponentDead) {
            
            if (blocked >= 0) {
                blocked--;
            }
            
            if (blocked < 0) {
                blocked = delayTicks - 1;
                
                Point newPosition = currentWalkStrategy.walk(x, y);
                
                x = newPosition.x;
                y = newPosition.y;
            }
            
            if (intersectsWithPlayer()) {
                // alert
                // GameControl.getControl().alert("New combo: ");
                
                // goal update
                // GameControl.getControl().updateGoal(Goal.OPPONENT);
                
                // game logic
                ScreenControl.getCtrl().addScreen("combat", new CombatScreen(monsterList));
                ScreenControl.getCtrl().setScreen("combat");
            }
            
        }
    }
    
    // @Override
    public boolean isAlive() {
        return monsterList.size() > 0;
    }
    
    public ArrayList<Monster> getMonsterList() {
        return monsterList;
    }
}
