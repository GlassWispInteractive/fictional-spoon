package display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dungeon.*;

/**
 * 
 * @author Danny Rademacher
 * 
 */
public class Main extends JFrame {
	private static final long serialVersionUID = 0L;
	private JPanel contentPane;

	private GameGui gui;
	Level level;
	private Generator gen = new Generator(277, 173);
	private Map cells = gen.newLevel();
	
	private static Dimension frameSize = new Dimension(1400, 900);
	
    private static final long secInNanosec = 1000000000L;
    private static final long milisecInNanosec = 1000000L;
    private final int GAME_FPS = 5;
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;
    

	public Main() {
		// initializing frame
		super("in my mind - there is a spoon");
		
		level = Level.getLevel(cells);
		gui = new GameGui(level);

		// basic layout with panes
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 50, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(2, 2, 2, 2));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// set pane for graphics
		contentPane.add(gui, BorderLayout.CENTER);
		gui.setPreferredSize(frameSize);
		

		// turn on animation
		Thread task = new Thread() {
			public void run() {
				gameLoop();
			}
		};
		task.start();
	}

	private void gameLoop(){
		
		long beginTime, timeTaken, timeLeft;
		while(true){
			beginTime = System.nanoTime();
			
			
			//update
			
			
			
			//repaint
			gui.revalidate();
			gui.repaint();			
			
			timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec;
            
            if (timeLeft < 10) 
                timeLeft = 10; //set a minimum
            try {
                 //Provides the necessary delay and also yields control so that other thread can do work.
                 Thread.sleep(timeLeft);
            } catch (InterruptedException ex) { }
			
		}
		
	}

	public static void main(String[] args) {
		// EventQueue - like the pros
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// native look is cute
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (UnsupportedLookAndFeelException e) {
						e.printStackTrace();
					}

					// initialize MainFrame
					JFrame frame = new Main();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setSize(frameSize);
					frame.setResizable(false);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
}
