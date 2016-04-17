package game.screens;

import core.Context;
import core.Events;
import core.Tiles;
import game.control.GameControl;
import game.control.Screen;
import game.control.ScreenControl;
import game.entities.Entity;
import game.entities.Player;
import game.generator.Ground;
import game.generator.Map;
import javafx.scene.canvas.GraphicsContext;

public class MapScreen extends Screen {
    
    // map settings
    private final int size = 16;
    
    // class components
    private Map map;
    
    // variables
    private int cameraX, cameraY, cameraSizeX, cameraSizeY;
    
    public MapScreen(Map map) {
        // call parent constructor
        super();
        
        this.map = map;
        
        // set layout
        addCanvas("map", 0, 0, size * map.getN(), size * map.getM());
        addCanvas("entities", 0, Context.PANEL_HEIGHT, Context.GAME_WIDTH, Context.GAME_HEIGHT);
        
        // render the map prior every other rendering and keep it cached
        prerenderMap();
        
        
        // set view size and be sure to be smaller than the map
        cameraSizeX = Math.min(Context.GAME_WIDTH / size, map.getN());
        cameraSizeY = Math.min(Context.GAME_HEIGHT / size, map.getM());
        
        // set view
        Entity player = Player.getNewest();
        initCamera(player.getX(), player.getY());
    }
    
    /**
     * @return the map
     */
    public Map getMap() {
        return map;
    }
    
    /**
     * method is called every tick
     * 
     * @param ticks
     */
    @Override
    public void tick(int ticks) {
        Player.getNewest().tick(ticks);
        
        // let all the entities tick
        for (Entity mob : Entity.getObjects()) {
            mob.tick(ticks);
        }
        
        //
        // fac.smartAdd();
        // fac.smartDelete();
        
        if (Events.isC()) {
            ScreenControl.getCtrl().setScreen("combo");
            Events.clear();
        }
        
        if (Events.isQ()) {
            GameControl.getControl().alert("Quest: " + GameControl.getControl().getQuest());
            Events.clear();
        }
    }
    
    /**
     * initiaze the camera view
     * 
     * @param centerX
     * @param centerY
     */
    public void initCamera(int centerX, int centerY) {
        this.cameraX = centerX - cameraSizeX / 2;
        this.cameraY = centerY - cameraSizeY / 2;
        
        alignCamera();
    }
    
    /**
     * updates the camera view the view is only chaning when the player moves
     * close to the border
     * 
     * @param centerX
     * @param centerY
     */
    public void updateCamera(int centerX, int centerY) {
        // 20% of the screen is the
        final int viewPaddingX = cameraSizeX / 5;
        final int viewPaddingY = cameraSizeY / 5;
        
        if (centerX - viewPaddingX < cameraX) {
            cameraX = centerX - viewPaddingX;
        }
        if (centerX + viewPaddingX - cameraSizeX > cameraX) {
            cameraX = centerX + viewPaddingX - cameraSizeX;
        }
        if (centerY - viewPaddingY < cameraY) {
            cameraY = centerY - viewPaddingY;
        }
        if (centerY + viewPaddingY - cameraSizeY > cameraY) {
            cameraY = centerY + viewPaddingY - cameraSizeY;
        }
        
        alignCamera();
    }
    
    /**
     * aligns the camera according to the displayable screen
     */
    private void alignCamera() {
        if (cameraX < 0) {
            cameraX = 0;
        }
        if (cameraY < 0) {
            cameraY = 0;
        }
        
        if (cameraX >= map.getN() - cameraSizeX) {
            cameraX = map.getN() - cameraSizeX;
        }
        if (cameraY >= map.getM() - cameraSizeY) {
            cameraY = map.getM() - cameraSizeY;
        }
    }
    
    @Override
    public void render() {
        // shift prerendered map
        nodes.get("map").relocate(-16 * cameraX, Context.PANEL_HEIGHT - 16 * cameraY);
        
        // render entities
        renderEntities();
        
    }
    
    /**
     * prerenders the map to make it unnecessary to rerender the same map every
     * tick (huge improvement)
     */
    private void prerenderMap() {
        // initialize render screen
        final GraphicsContext gc = gcs.get("map");
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        // full rendering of the map
        for (int x = 0; x < map.getN(); x++) {
            for (int y = 0; y < map.getM(); y++) {
                if (map.getGround(x, y) != Ground.WALL) {
                    final int tile = 20 + 57 * 12 + map.getTileNumber(x, y);
                    
                    // draw tile image
                    gc.drawImage(Tiles.get("map", tile % 57, tile / 57), x * size, y * size);
                }
            }
        }
        
         // initialize render screen
        // Group tilemap = new Group();
        // group.getChildren().clear();
        // group.getChildren().add(tilemap);
        // nodes.put("map", tilemap);
    }
    
    /**
     * render the entities
     */
    private void renderEntities() {
        // initialize render screen
        final GraphicsContext gc = gcs.get("entities");
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        
        // render all entities
        for (Entity mob : Entity.getObjects()) {
            mob.render(gc, size, cameraX, cameraY);
        }
        
        // render player
        Player.getNewest().render(gc, size, cameraX, cameraY);
    }
}
