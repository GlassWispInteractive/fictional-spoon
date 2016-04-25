package game.screens;

import core.Global;
import game.control.Screen;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.TextAlignment;

public class CreditsScreen extends Screen {
    
    private static CreditsScreen singleton;
    
    public static CreditsScreen getScreen() {
        
        if (singleton == null) {
            singleton = new CreditsScreen();
        }
        
        return singleton;
    }
    
    private CreditsScreen() {
    
    }
    
    @Override
    public void tick(int ticks) {
        // SpookingSouls.getObject().tick(ticks);
    }
    
    @Override
    public void render() {
        // start from clean screen
        GraphicsContext gc = gcs.get("main");
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        
        // font settings
        gc.setFont(Global.HUGE_FONT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.BASELINE);
        // gc.setLineWidth(1);
        
        // Title
        gc.setFill(Global.RED);
        gc.setStroke(Global.RED);
        gc.fillText(Global.TITLE, Global.WINDOW_WIDTH / 2, 75);
        gc.strokeLine(0, 75 + 20, Global.WINDOW_WIDTH, 75 + 20);
        
        // single elements
        final String credits[] = new String[] { "coding by dhaunac", "coding by garax91", "tile sets by kenney.nl",
                "element pictures from Korra" };
                
        gc.setFont(Global.DEFAULT_FONT);
        for (int i = 0; i < credits.length; i++) {
            gc.fillText(credits[i], Global.WINDOW_WIDTH / 2, 200 + i * 100);
        }
        
    }
    
}
