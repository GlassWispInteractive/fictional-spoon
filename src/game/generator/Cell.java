package game.generator;

public class Cell {
    private Ground ground;
    
    /**
     * public constructor, default ground is wall (no passing)
     * 
     * @param x
     * @param y
     */
    public Cell(int x, int y) {
        this.ground = Ground.WALL;
    }
    
    /**
     * setter method for ground state
     * 
     * @param ground
     */
    public void setGround(Ground ground) {
        this.ground = ground;
    }
    
    /**
     * getter method for ground state
     * 
     * @return
     */
    public Ground getGround() {
        return ground;
    }
}
