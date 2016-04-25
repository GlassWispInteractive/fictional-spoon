package game.entities;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;

/**
 * class for Mobile objects
 * 
 * @author danny
 *        
 */
public abstract class Entity {
    private static ArrayList<Entity> collection = new ArrayList<>();
    
    // class members
    protected int x, y;
    protected int delayTicks;
    
    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
        
        collection.add(this);
    }
    
    /**
     * function returns every existing monster
     * 
     * @return
     */
    public static Entity[] getObjects() {
        return collection.toArray(new Entity[] {});
    }
    
    /**
     * function resets the state
     */
    public static void reset() {
        collection = new ArrayList<>();
        
        Portal.resetCount();
    }
    
    /**
     * render method for every mobile object because its visible
     * 
     * @param GraphicsContext
     */
    public abstract void render(GraphicsContext gc, int size, int offsetX, int offsetY);
    
    /**
     * tick is called every tick
     * 
     * @param elapsedTime
     */
    public abstract void tick(int ticks);
    
    /**
     * @return the x
     */
    public int getX() {
        return x;
    }
    
    /**
     * @param x
     *            the x to set
     */
    public void setX(int x) {
        this.x = x;
    }
    
    /**
     * @return the y
     */
    public int getY() {
        return y;
    }
    
    /**
     * @param y
     *            the y to set
     */
    public void setY(int y) {
        this.y = y;
    }
    
    protected boolean intersectsWithPlayer() {
        return (x == Player.getNewest().getX() && y == Player.getNewest().getY());
    }
    
    public static void remove(Player player) {
        collection.remove(player);
    }
    
}
