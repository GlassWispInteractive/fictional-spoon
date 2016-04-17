package game.screens;

import core.Global;
import core.Events;
import game.control.Screen;
import game.control.ScreenControl;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.TextAlignment;

public class HelpScreen extends Screen {
    // class members
    private int duration, remaining;
    private String nextScreen;
    private String text[];
    private int index;
    
    public HelpScreen(String next, int duration) {
        super();
        
        text = new String[] { "" };
        this.duration = duration;
        this.nextScreen = next;
        
        remaining = this.duration;
    }
    
    @Override
    public void tick(int ticks) {
        // on enter skip the screen
        if (Events.isEnter()) {
            remaining = 0;
            Events.clear();
        }
        
        if (remaining > 0) {
            index = (int) Math.floor((double) text.length * (duration - remaining) / duration);
            
            remaining -= ticks;
        } else {
            ScreenControl.getCtrl().setScreen(nextScreen);
            remaining = duration;
        }
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
        
        // text
        gc.setFill(Global.RED);
        gc.setStroke(Global.RED);
        
        gc.fillText(text[index], Global.WINDOW_WIDTH / 2, Global.WINDOW_HEIGHT * 0.4);
        // gc.strokeLine(0, 75 + 20, Window.SIZE_X, 75 + 20);
        
        // gc.setFont(Window.DEFAULT_FONT);
        // for (int i = 0; i < text.length; i++) {
        // gc.fillText(text[i], Window.SIZE_X / 2, 200 + i * 100);
        // }
    }
    
    public void setText(String[] text) {
        this.text = text;
    }
    
    /**
     * @param nextScreen
     *            the nextScreen to set
     */
    public void setNextScreen(String nextScreen) {
        this.nextScreen = nextScreen;
    }
}
