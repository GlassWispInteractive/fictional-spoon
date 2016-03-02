package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;


public abstract class Canvas extends JPanel implements KeyListener{

	private static final long serialVersionUID = 1L;
	
	// Keyboard states - Here are stored states for keyboard keys - is it down or not.
    private static boolean[] keyboardState = new boolean[525];
        

	public Canvas()
    {
        // We use double buffer to draw on the screen.
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setBackground(Color.black);
        
        // Adds the keyboard listener to JPanel to receive key events from this component.
        this.addKeyListener(this);
    }
    
    
    // This method is overridden in Framework.java and is used for drawing to the screen.
    public abstract void draw(Graphics2D g2d);
    
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;        
        super.paintComponent(g2d);        
        draw(g2d);
    }
       
    
    // Keyboard
    /**
     * Is keyboard key "key" down?
     * 
     * @param key Number of key for which you want to check the state.
     * @return true if the key is down, false if the key is not down.
     */
    public static boolean keyboardKeyState(int key)
    {
        return keyboardState[key];
    }
    
    // Methods of the keyboard listener.
    @Override
    public void keyPressed(KeyEvent e) 
    {
        keyboardState[e.getKeyCode()] = true;
    }
    
    @Override
    public void keyReleased(KeyEvent e)
    {
        keyboardState[e.getKeyCode()] = false;
        keyReleasedFramework(e);
    }
    
    @Override
    public void keyTyped(KeyEvent e) { }
    
    public abstract void keyReleasedFramework(KeyEvent e);
    
}
