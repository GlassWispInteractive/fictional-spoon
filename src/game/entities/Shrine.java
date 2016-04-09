package game.entities;

import engine.TileMaster;
import framework.GameControl;
import game.combat.Quest;
import javafx.scene.canvas.GraphicsContext;

public class Shrine extends Entity {
    
    private int blocked = 0;
    private TileMaster tileFac;
    private Player player = Player.getNewest();
    
    public Shrine(int x, int y) {
        super(x, y);
        delayTicks = 1500;
        tileFac = TileMaster.getInstance();
        
    }
    
    @Override
    public void render(GraphicsContext gc, int size, int offsetX, int offsetY) {
        
        if (blocked == 0) {
            gc.drawImage(tileFac.getTile("map", 43, 10), size *(x - offsetX), size *(y - offsetY));
        } else {
            gc.drawImage(tileFac.getTile("map", 41, 10), size *(x - offsetX), size *(y - offsetY));
        }
        
    }
    
    @Override
    public void tick(int ticks) {
        if (blocked > 0) {
            blocked -= ticks;
            return;
        }
        
        // check intersection
        if (intersectsWithPlayer()) {
            // game logic
            player.heal();
            blocked = delayTicks;
            
            // alert
            GameControl.getControl().alert("Player health restored");
            
            // goal update
            GameControl.getControl().updateGoal(Quest.Goal.SHRINE);
        }
        
    }
    
}
