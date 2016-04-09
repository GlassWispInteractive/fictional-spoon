package screens;

import engine.ImageSource;
import engine.TileFactory;
import engine.TileSource;
import framework.EventControl;
import framework.GameControl;
import framework.Global;
import framework.Screen;
import framework.ScreenControl;
import game.entities.Entity;
import game.entities.Player;
import generation.Ground;
import generation.Map;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class MapScreen extends Screen {
    
    // map settings
    private final int size = 16;
    
    // class components
    private Map map;
    private TileFactory tileFac = TileFactory.getTilesFactory();;
    
    // variables
    private int cameraX, cameraY, cameraSizeX, cameraSizeY;
    
    public MapScreen(Map map) {
        // call parent constructor
        super();
        
        this.map = map;
        
        // render the map prior every other rendering and keep it cached
        prerenderMap();
        
        // set layout
        addCanvas("entities", 0, Global.PANEL_HEIGHT, Global.GAME_WIDTH, Global.GAME_HEIGHT);
        
        // load factories
        tileFac = TileFactory.getTilesFactory();
        
        // set view size and be sure to be smaller than the map
        cameraSizeX = Math.min(Global.GAME_WIDTH / size, map.getN());
        cameraSizeY = Math.min(Global.GAME_HEIGHT / size, map.getM());
        
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
        
        if (EventControl.getEvents().isC()) {
            ScreenControl.getCtrl().setScreen("combo");
            EventControl.getEvents().clear();
        }
        
        if (EventControl.getEvents().isQ()) {
            GameControl.getControl().alert("Quest: " + GameControl.getControl().getQuest());
            EventControl.getEvents().clear();
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
        nodes.get("map").relocate(-16 * cameraX, Global.PANEL_HEIGHT - 16 * cameraY);
        
        // render entities
        renderEntities();
        
    }
    
    /**
     * prerenders the map to make it unnecessary to rerender the same map every
     * tick (huge improvement)
     */
    private void prerenderMap() {
        // initialize render screen
        Group tilemap = new Group();
        group.getChildren().clear();
        group.getChildren().add(tilemap);
        nodes.put("map", tilemap);
        
        // full rendering of the map
        for (int x = 0; x < map.getN(); x++) {
            for (int y = 0; y < map.getM(); y++) {
                if (map.getGround(x, y) != Ground.WALL) {
                    int tile = 20 + 57 * 12 + map.getTileNumber(x, y);
                    ImageView img = new ImageView(
                            tileFac.getImage(new ImageSource(TileSource.MAP_TILES, tile % 57, tile / 57)));
                    tilemap.getChildren().add(img);
                    img.relocate(size * x, size * y);
                } else {
                    Rectangle rec = new Rectangle(size, size);
                    rec.setFill(Global.DARKGRAY);
                    tilemap.getChildren().add(rec);
                    rec.relocate(size * x, size * y);
                }
            }
        }
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
    
    /**
     * helper function to draw tiles onto the gc object
     * 
     * @param gc
     * @param x
     * @param y
     * @param ground
     * @param tile
     */
    private void drawTile(GraphicsContext gc, int x, int y, Ground ground, int tile) {
        
        // offset in tile set
        if (ground == Ground.FLOOR)
            tile += 20 + 57 * 12;
        if (ground == Ground.ROOM)
            tile += 20 + 57 * 12;// +-7
            
        ImageSource imgsource = new ImageSource(TileSource.MAP_TILES, tile % 57, tile / 57);
        
        // System.out.println(tileFac);
        // System.out.println(gc);
        // System.out.println(imgsource);
        // System.out.println(x);
        // System.out.println(y);
        // System.out.println(size);
        // System.out.println();
        
        tileFac.drawTile(gc, imgsource, x, y, size);
    }
}
