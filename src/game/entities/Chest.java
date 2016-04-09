package game.entities;

import java.util.ArrayList;

import engine.TileFactory;
import engine.TileSource;
import framework.GameControl;
import game.combat.ComboAttack;
import game.combat.Quest;
import javafx.scene.canvas.GraphicsContext;

public class Chest extends Entity {
    private static ArrayList<Chest> collection = new ArrayList<>();;
    
    private ComboAttack item;
    TileFactory tileFac;
    
    private Chest(int x, int y, ComboAttack combo) {
        super(x, y);
        this.item = combo;
        
        tileFac = TileFactory.getTilesFactory();
    }
    
    /**
     * function generates a monster at a specified place
     * 
     * @param x
     * @param y
     * @param spawnIsInRoom
     */
    public static void generate(int x, int y) {
        Chest obj = new Chest(x, y, new ComboAttack());
        collection.add(obj);
    }
    
    /**
     * function returns every existing monster
     * 
     * @return
     */
    public static Chest[] getObjects() {
        return collection.toArray(new Chest[] {});
    }
    
    /**
     * function resets the state
     */
    public static void reset() {
        collection = new ArrayList<>();
    }
    
    @Override
    public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {
        
        if (item == null) {
            gc.drawImage(tileFac.getImage(TileSource.MAP_TILES, 15, 7), size *(x - offsetX), size *(y - offsetY));
        } else {
            gc.drawImage(tileFac.getImage(TileSource.MAP_TILES, 14, 6), size *(x - offsetX), size *(y - offsetY));
        }
    }
    
    @Override
    public void tick(int ticks) {
        // check intersection
        if (intersectsWithPlayer() && item != null) {
            // alert
            GameControl.getControl().alert("New combo: " + item.toString());
            
            // goal update
            GameControl.getControl().updateGoal(Quest.Goal.CHEST);
            
            // game logic
            item.activate();
            item = null;
        }
        
    }
    
}
