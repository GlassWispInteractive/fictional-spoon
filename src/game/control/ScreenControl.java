package game.control;

import java.util.HashMap;

import core.Window;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class ScreenControl {
    // singleton object
    private static ScreenControl singleton;
    
    // game state
    private HashMap<String, Screen> screens;
    private Screen screen;
    private boolean ticking = true;
    
    private ScreenControl() {
        screens = new HashMap<>();
    }
    
    public static ScreenControl getCtrl() {
        if (singleton == null) {
            singleton = new ScreenControl();
        }
        
        return singleton;
    }
    
    /**
     * @return the state
     */
    public Screen getScreen() {
        return screen;
    }
    
    /**
     * add new screen with string id
     * 
     * @param name
     * @param screen
     */
    public void addScreen(String name, Screen screen) {
        screens.put(name, screen);
        screen.getScene().getRoot().setOpacity(0);
    }
    
    /**
     * remove a screen by string id
     * 
     * @param name
     */
    public void removeScreen(String name) {
        screens.remove(name);
    }
    
    /**
     * set a screen by string id
     * 
     * @param name
     */
    public void setScreen(String name) {
        // dont try to set new screen
        if (screens.get(name) == null) {
            System.out.println("invalid screen");
            return;
        }
        
        // fade out animation
        if (screen != null) {
            FadeTransition ft = new FadeTransition(new Duration(500), screen.getScene().getRoot());
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.play();
            
            ticking = false;
            
            ft.setOnFinished(e -> {
                fadeIn(name);
            });
        } else {
            fadeIn(name);
        }
    }
    
    private void fadeIn(String name) {
        screen = screens.get(name);
        Window.setScene(screen.getScene());
        
        screen.getScene().getRoot().setOpacity(1);
        ticking = true;
    }
    
    /**
     * set a new screen by giving it a name
     * 
     * @param name
     * @param screen
     */
    public void setScreen(String name, Screen screen) {
        // if scene id is already used, dont set screen
        if (screens.get(name) != null) {
            return;
        }
        
        // add screen first and then set it
        addScreen(name, screen);
        setScreen(name);
    }
    
    public void tick(int ticks) {
        if (ticking && screen != null) {
            screen.tick(ticks);
        }
    }
    
    public void render() {
        if (screen != null) {
            screen.render();
        }
    }
}
