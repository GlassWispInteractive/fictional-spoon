package game;

import java.awt.Graphics2D;

import dungeon.Generator;
import dungeon.Map;


public class Game {
	
	private Generator gen;
	private Map cells;
	private Level level;


    public Game()
    {
//        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
//        
//        Thread threadForInitGame = new Thread() {
//            @Override
//            public void run(){
//                // Sets variables and objects for the game.
//                initialize();
//                // Load game files (images, sounds, ...)
//                loadContent();
//                
//                Framework.gameState = Framework.GameState.PLAYING;
//            }
//        };
//        threadForInitGame.start();
    }
    
    
   /**
     * Set variables and objects for the game.
     */
    private void initialize()
    {
    	gen = new Generator(277, 173);
    	cells = gen.newLevel();
    	level = Level.getLevel(cells);
    }
    
    /**
     * Load game files - images, sounds, ...
     */
    private void loadContent()
    {
    
    }    
    
    
    /**
     * Restart game - reset some variables.
     */
    public void restartGame()
    {
        
    }
    
    
    /**
     * Update game logic.
     * 
     * @param gameTime gameTime of the game.
     */
    public void updateGame(long gameTime)
    {
    	level.changeCurrentView(1, 1);
    }
    
    /**
     * Draw the game to the screen.
     * 
     * @param g2d Graphics2D
     */
    public void draw(Graphics2D g2d)
    {
		if(level != null){
			//level.drawCompleteMap(g2);
			level.drawPlayerView(g2d);
		}
    }
}
