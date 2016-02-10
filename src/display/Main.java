package display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dungeon.Generator;
import dungeon.Level;

/**
 * 
 * @author Danny Rademacher
 * 
 */
public class Main extends JFrame {
	private static final long serialVersionUID = 0L;
	private JPanel contentPane;

	private Bitmap gui = new Bitmap();
	private Generator gen = new Generator(277, 173);
	private Level cells = gen.newLevel();

	private Timer animate = new Timer("Animate");
	private double time = 0.2;

	public Main() {
		// initializing frame
		super("a fictional spoon - and its bloodless");

		// basic layout with panes
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 50, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(2, 2, 2, 2));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// set pane for graphics
		contentPane.add(gui, BorderLayout.CENTER);
		gui.setPreferredSize(new Dimension(1400, 900));

		// turn on animation
		TimerTask task = new TimerTask() {
			public void run() {
				tick();
			}
		};

		gui.setRoom(cells);
		animate = new Timer();
		animate.scheduleAtFixedRate(task, 100, (long) (time * 1000));
	}

	private void tick() {
		// System.out.println("tick");
		gui.setRoom(cells);
		gui.revalidate();
		gui.repaint();

		// System.out.println(gui.getSize());

		// automaton.tick();

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
					frame.setSize(1400, 900);
					frame.setResizable(false);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
}
