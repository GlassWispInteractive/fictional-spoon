package game.control;

import core.Global;
import game.combat.ComboAttack;
import game.combat.Quest;
import game.entities.Entity;
import game.entities.Monster;
import game.entities.Portal;
import game.generator.LevelBuilder;
import game.generator.Map;
import game.screens.AlertDecorator;
import game.screens.HelpScreen;
import game.screens.MapScreen;
import game.screens.PanelDecorator;

public class GameControl {
    // singleton
    private static GameControl singleton;
    
    private HelpScreen screen;
    
    private Quest objective;
    private MapScreen map;
    private PanelDecorator panel;
    private AlertDecorator alert;
    private int level;
    
    private GameControl() {
        // call the very important state constructor
        super();
        
        // settings
        screen = new HelpScreen("game", 180);
        ScreenControl.getCtrl().addScreen("game intro", screen);
        level = 1;
        classicMode(level);
        
    }
    
    /**
     * @return the objective
     */
    public String getQuest() {
        return objective.toString();
    }
    
    /**
     * static method to get the singleton class object
     * 
     * @return
     */
    public static GameControl getControl() {
        if (singleton == null) {
            singleton = new GameControl();
        }
        return singleton;
    }
    
    /**
     * reset the game state
     */
    public static void resetGame() {
        // reset EntityFactory
        Entity.reset();
        ComboAttack.resetCombos();
        
        singleton = new GameControl();
    }
    
    /**
     * starts the classMode at a given level
     * 
     * @param level
     */
    private void classicMode(int level) {
        //
        ScreenControl ctrl = ScreenControl.getCtrl();
        
        if (level > 1) {
            // reset stuff
            ctrl.setScreen("game intro");
            
            Entity.reset();
            
            ComboAttack.resetCombos();
        }
        
        // set up generator power to level
        Monster.setPower(level);
        ComboAttack.setLength(1 + level);
        
        // set the appropriate objective by level
        switch (level) {
        
        // basic level
        case 1:
            map = new MapScreen(new LevelBuilder(Global.GAME_WIDTH / 16, Global.GAME_HEIGHT / 16,
                    LevelBuilder.Layout.SINGLE_CONN_ROOMS).genMonster(1, 0.01).genShrine(0.1, 0).create());
            objective = new Quest(Quest.Goal.MONSTER, 3);
            
            break;
            
        // default level
        case 2:
            ctrl.setScreen("game intro");
            
            map = new MapScreen(new LevelBuilder(300, 200, LevelBuilder.Layout.LOOPED_ROOMS).genMonster(1.3, 0.1)
                    .genChest(0.2, 0.01).create());
            objective = new Quest(Quest.Goal.MONSTER, 30);
            break;
            
        // deactivate portals
        case 3:
            ctrl.setScreen("game intro");
            
            map = new MapScreen(new LevelBuilder(Global.GAME_WIDTH / 16, Global.GAME_HEIGHT / 16,
                    LevelBuilder.Layout.MAZE_WITH_ROOMS).genMonster(2, 0.1).genChest(0, 0.05).genShrine(0, 0.01)
                            .genPortal(1, 0).create());
            // calculate the number of portals created
            objective = new Quest(Quest.Goal.PORTAL, Portal.getCount());
            
            break;
            
        // default level
        case 4:
            ctrl.setScreen("game intro");
            
            map = new MapScreen(new LevelBuilder(300, 200, LevelBuilder.Layout.LOOPED_ROOMS).genMonster(1.3, 0.1)
                    .genChest(0.2, 0.01).create());
            objective = new Quest(Quest.Goal.MONSTER, 50);
            break;
            
        // boss maze
        case 5:
            ctrl.setScreen("game intro");
            
            map = new MapScreen(
                    new LevelBuilder(Global.GAME_WIDTH / 16, Global.GAME_HEIGHT / 16, LevelBuilder.Layout.MAZE)
                            .genChest(0, 0.01).genOpponent(0, 0.005).create());
            objective = new Quest(Quest.Goal.OPPONENT, 1);
            break;
        }
        
        // update game screens
        screen.setText(new String[] { "Quest: " + objective });
        panel = new PanelDecorator(map);
        alert = new AlertDecorator(panel);
        ctrl.addScreen("game", alert);
        
        // update references
        panel.updateProgress(level, objective.progress());
    }
    
    /**
     * starts the arcade mode to fight againsts bosses only
     * 
     * @param level
     */
    public void arcadeMode(int level) {
        // see issue for more information
    }
    
    /**
     * @return the map
     */
    public Map getMap() {
        // return map;
        return map.getMap();
    }
    
    public void updateCamera(int x, int y) {
        map.updateCamera(x, y);
    }
    
    public void alert(String string) {
        alert.push(string);
    }
    
    public void updateGoal(Quest.Goal goal) {
        // update objective reference
        objective.add(goal);
        
        if (objective.progress() < 1) {
            // update panel, game is going
            panel.updateProgress(level, objective.progress());
        } else {
            // objective is reached
            if (level == 6) {
                ScreenControl.getCtrl().setScreen("game won");
            } else {
                level++;
                classicMode(level);
            }
        }
    }
    
}
