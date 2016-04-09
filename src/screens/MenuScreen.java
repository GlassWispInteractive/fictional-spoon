package screens;

import java.util.ArrayList;
import java.util.Arrays;

import framework.EventControl;
import framework.Global;
import framework.Screen;
import framework.ScreenControl;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.TextAlignment;

//import static game.State.*;

public class MenuScreen extends Screen {
    // singleton
    private static MenuScreen singleton;
    
    private Image logo;
    
    private ArrayList<String> list;
    private int cur;
    
    public static MenuScreen getScreen() {
        if (singleton == null) {
            singleton = new MenuScreen();
        }
        
        return singleton;
    }
    
    private MenuScreen() {
        super();
        
        // init
        setList(new String[] { "Classic Mode", "Arcade Mode", "Credits", "Help", "Exit" });
        
        logo = new Image("/resources/elem/logo.png");
    }
    
    @Override
    public void tick(int ticks) {
        EventControl e = EventControl.getEvents();
        
        // event handling
        if (e.isUp())
            cur = (cur + list.size() - 1) % list.size();
            
        if (e.isDown())
            cur = (cur + 1) % list.size();
            
        if (e.isEnter()) {
            switch (list.get(cur)) {
            case "Classic Mode":
                ScreenControl.getCtrl().setScreen("game intro");
                break;
                
            case "Credits":
                ScreenControl.getCtrl().setScreen("credits");
                break;
                
            case "Help":
                ScreenControl.getCtrl().setScreen("help");
                break;
                
            case "Exit":
                System.exit(0);
                break;
            default:
                break;
            }
        }
        
        // no double key activation
        EventControl.getEvents().clear();
    }
    
    @Override
    public void render() {
        // start from clean screen
        final GraphicsContext gc = gcs.get("main");
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        
        // canvas settings
        final double w = gc.getCanvas().getWidth();
        
        // render logo image
        gc.drawImage(logo, (w - logo.getWidth()) / 2, 80);
        
        // font type
        gc.setFont(Global.DEFAULT_FONT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        
        gc.setLineWidth(1);
        
        for (int i = 0; i < list.size(); i++) {
            // render box
            gc.setFill(Global.WHITE.deriveColor(0, 1, 1, 0.5));
            gc.setStroke(Global.WHITE.brighter().deriveColor(0, 1, 1, 0.5));
            gc.fillRoundRect((w - 200) / 2, 200 + 90 * (i + 1), 200, 60, 60, 200);
            gc.strokeRoundRect((w - 200) / 2, 200 + 90 * (i + 1), 200, 60, 60, 200);
            
            // render text on box
            gc.setFill(i != cur ? Global.DARKRED : Global.DARKRED.brighter());
            gc.fillText(list.get(i), w / 2, 200 + 90 * (i + 1) + 30);
        }
        
    }
    
    private void setList(String[] strings) {
        list = new ArrayList<String>(Arrays.asList(strings));
        cur = 0;
    }
}
