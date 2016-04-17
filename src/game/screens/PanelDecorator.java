package game.screens;

import core.Context;
import game.control.Screen;
import game.control.ScreenDecorator;
import game.entities.Player;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class PanelDecorator extends ScreenDecorator {
    private double progress;
    private int level, life;
    
    public PanelDecorator(Screen decoratedScreen) {
        super(decoratedScreen);
        
        addCanvas("top panel", 0, 0, Context.WINDOW_WIDTH, 3 * 16);
        addCanvas("bottom panel", 0, Context.WINDOW_HEIGHT - Context.PANEL_HEIGHT, Context.WINDOW_WIDTH,
                Context.PANEL_HEIGHT);
        progress = 0;
    }
    
    public void updateProgress(int level, double progress) {
        this.level = level;
        this.progress = progress;
        this.life = Player.getNewest().getLife();
    }
    
    @Override
    public void tick(int ticks) {
        // call decorated screen
        super.tick(ticks);
        
    }
    
    @Override
    public void render() {
        // call decorated screen
        super.render();
        
        renderTopPanel();
        renderBottomPanel();
    }
    
    private void renderTopPanel() {
        // clear screen
        GraphicsContext gc = gcs.get("top panel");
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        
        // draw background
        gc.setFill(Context.DARKGRAY);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(Context.WHITE);
        gc.fillRect(0, Context.PANEL_HEIGHT - 5, gc.getCanvas().getWidth(), 2);
        
        // print out label
        gc.setFont(Context.DEFAULT_FONT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("quest progress", 125, -5 + (Context.PANEL_HEIGHT - 5) / 2);
        
        // draw objective progress
        gc.setLineWidth(3);
        gc.setStroke(Context.WHITE);
        gc.strokeRect(250, 5, Context.WINDOW_WIDTH - 300, 30);
        
        // progress bar
        gc.setFill(Color.DARKGREEN.deriveColor(1, progress, 2, 1));
        gc.fillRect(250, 10, progress * (Context.WINDOW_WIDTH - 300), 20);
        
        // draw the delimters each 5 percents
        // use ints, because of double problems with exact numbers
        gc.setLineWidth(2);
        gc.setStroke(Context.WHITE);
        for (int i = 5; i < 100; i += 5) {
            gc.strokeLine(250 + i / 100.0 * (Context.WINDOW_WIDTH - 300), 5,
                    250 + i / 100.0 * (Context.WINDOW_WIDTH - 300), 35);
        }
    }
    
    private void renderBottomPanel() {
        // clear screen
        GraphicsContext gc = gcs.get("bottom panel");
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        
        // draw background
        gc.setFill(Context.DARKGRAY);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(Context.WHITE);
        gc.fillRect(0, 5, gc.getCanvas().getWidth(), 2);
        
        // font settings
        gc.setFont(Context.DEFAULT_FONT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        
        // print out text boxes
        gc.fillText("level " + level, Context.GAME_WIDTH * .2, 5 + (Context.PANEL_HEIGHT - 5) / 2);
        gc.fillText(life + " life", Context.GAME_WIDTH * .4, 5 + (Context.PANEL_HEIGHT - 5) / 2);
        gc.fillText("combos (C)", Context.GAME_WIDTH * .6, 5 + (Context.PANEL_HEIGHT - 5) / 2);
        gc.fillText("quest (Q)", Context.GAME_WIDTH * .8, 5 + (Context.PANEL_HEIGHT - 5) / 2);
    }
    
}
