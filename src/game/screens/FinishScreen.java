package game.screens;

import java.util.ArrayList;
import java.util.Arrays;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;

import core.Global;
import core.Events;
import game.control.GameControl;
import game.control.Screen;
import game.control.ScreenControl;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.TextAlignment;

public class FinishScreen extends Screen {
    private final String won = "You have won the game", lost = "Game over";
    private boolean finish = false;
    
    private int cur;
    private ArrayList<String> list = new ArrayList<String>(Arrays.asList(new String[] { "Restart", "Credit", "Exit" }));
    
    public FinishScreen(boolean finish) {
        this.finish = finish;
    }
    
    @Override
    public void tick(int ticks) {
        
        // SpookingSouls.getObject().tick(ticks);
        // event handling
        if (Events.isLeft())
            cur = (cur + list.size() - 1) % list.size();
            
        if (Events.isRight())
            cur = (cur + 1) % list.size();
            
        if (Events.isEnter()) {
            // TODO: put resetgame properly
            GameControl.resetGame();
            
            switch (list.get(cur)) {
            case "Restart":
                ScreenControl.getCtrl().setScreen("menu");
                break;
            case "Credit":
                ScreenControl.getCtrl().setScreen("credits");
                break;
            case "Exit":
                System.exit(0);
                break;
            default:
                break;
            }
        }
        
        // no double key activation
        Events.clear();
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
        
        // SpookingSouls.getObject().render(gc);
        
        // Game Over
        gc.setFill(Global.RED);
        gc.fillText(finish ? won : lost, Global.WINDOW_WIDTH / 2, Global.WINDOW_HEIGHT / 2);
        
        // List
        // calc textLength
        
        gc.setFont(Global.DEFAULT_FONT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setLineWidth(1);
        
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        int textWidth = (int) fontLoader.computeStringWidth("", gc.getFont());
        int textHeight = (int) fontLoader.getFontMetrics(gc.getFont()).getLineHeight();
        
        for (int i = 0; i < list.size(); i++) {
            if (textWidth < (int) fontLoader.computeStringWidth(list.get(i).toString(), gc.getFont())) {
                textWidth = (int) fontLoader.computeStringWidth(list.get(i).toString(), gc.getFont());
            }
        }
        
        int padding = 10;
        int width = textWidth + 6 * padding;
        int height = (int) (1.5 * textHeight);
        int rowY = (int) (Global.WINDOW_HEIGHT * 0.8);
        int columnX = Global.WINDOW_WIDTH / 2 - list.size() * width / 2;
        
        for (int j = 0; j < Math.min(10, list.size()); j++) {
            
            gc.setFill(Global.DARKGRAY.deriveColor(0, 1.2, 1, 0.6));
            gc.setStroke(Global.DARKGRAY);
            gc.fillRoundRect(columnX + padding, rowY, width - padding, height, 60, 200);
            
            if (j != cur) {
                gc.setFill(Global.DARKRED.deriveColor(0, 1.2, 1, 0.6));
                gc.setStroke(Global.DARKRED);
            } else {
                gc.setFill(Global.RED.deriveColor(0, 1.2, 1, 0.6));
                gc.setStroke(Global.RED);
            }
            
            gc.fillText(list.get(j).toString(), columnX + width / 2, rowY + height / 2);
            gc.strokeText(list.get(j).toString(), columnX + width / 2, rowY + height / 2);
            
            columnX += width;
        }
    }
}
