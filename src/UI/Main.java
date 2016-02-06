package UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Map.Level;

/**
 * 
 * @author Danny Rademacher
 * 
 */
public class Main extends JFrame {
	private static final long serialVersionUID = 0L;
	private JPanel contentPane;


	private Bitmap gui = new Bitmap();
	private Level cells = new Level(50, 100);



	private Timer animate = new Timer("Animate");
	private double time = 0.2;


	public Main() {
		// initializing frame
		super("spoon may be not real");

		// menu bar
		JMenuBar menuBar = new JMenuBar();
		JMenu menuCtrl = new JMenu("Control"), menuManipulate = new JMenu("Manipulate");
		menuBar.add(menuCtrl);
		menuBar.add(menuManipulate);
//		JMenu invisible
//		setJMenuBar(menuBar);

		// first menu
		JMenuItem ctrlToggle = new JMenuItem("Toggle playing");
		JMenuItem ctrlTime = new JMenuItem("Change update time");
		JMenuItem ctrlResize = new JMenuItem("Resize grid");

		menuCtrl.add(ctrlToggle);
		menuCtrl.add(ctrlTime);
		menuCtrl.add(ctrlResize);

		// second menu
		JMenuItem manipRandom = new JMenuItem("Load random");
		JMenuItem manipLoad = new JMenuItem("Load file");
		JMenuItem manipEdit = new JMenuItem("Edit current");
		JMenuItem manipSave = new JMenuItem("Save state into file");

		menuManipulate.add(manipRandom);
		menuManipulate.add(manipLoad);
		menuManipulate.add(manipEdit);
		menuManipulate.add(new JSeparator());
		menuManipulate.add(manipSave);

		// basic layout
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 50, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(2, 2, 2, 2));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(gui, BorderLayout.CENTER);
		gui.setPreferredSize(new Dimension(1200, 800));

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
		// automaton.print();
		gui.setRoom(cells);
		gui.revalidate();
		gui.repaint();

//		automaton.tick();

	}

	public static void main(String[] args) {
		// EventQueue - like the pros
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// native look
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
					frame.setSize(1200, 800);
					frame.setResizable(false);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
}
