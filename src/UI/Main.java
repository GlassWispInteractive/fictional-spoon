package UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * 
 * @author Danny Rademacher
 * 
 */
public class Main extends JFrame {
	private static final long serialVersionUID = 0L;
	private JPanel contentPane;
	private JScrollPane scrollPane;

	JList<String> itemList;

	private Bitmap gui = new Bitmap();
	private boolean[][] cells = {{true, true}};

	private Boolean bToggle = false;
	private double time = 2;

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	private Timer animate = new Timer("Animate");

	public void updateButtons() {

	}

	/**
	 * @param itemList
	 *            the itemList to set
	 */
	public void setItemList(Double[] w, Double[] p, int[] l) {
		DefaultListModel<String> listModel = new DefaultListModel<String>();

		for (int i = 0; i < w.length; i++) {
			String s = String.format("%d. weight %.2f; profit %.2f; len %d", (i + 1), w[i], p[i], l[i]);
			listModel.addElement(s);
		}

		itemList = new JList<String>(listModel);
		itemList.setEnabled(false);
		itemList.setSelectedIndex(0);
		itemList.setFixedCellHeight(20);

		// center alignment
		((DefaultListCellRenderer) itemList.getCellRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		scrollPane.setViewportView(itemList);
	}

	public void setListHighlight(int i) {
		// check for viable index
		if (i < 0) {
			return;
		}

		itemList.setSelectedIndex(i);
	}

	public Main() {
		// initializing frame
		super("spoon may be not real");

		// menu bar
		JMenuBar menuBar = new JMenuBar();
		JMenu menuCtrl = new JMenu("Control"), menuManipulate = new JMenu("Manipulate");
		menuBar.add(menuCtrl);
		menuBar.add(menuManipulate);
		setJMenuBar(menuBar);

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

	/**
	 * function toggle the animation hint: calling it twice results in an
	 * updated animation timer
	 */
	public void toggle() {
		TimerTask task = new TimerTask() {
			public void run() {
				tick();
			}
		};

		if (bToggle) {
			animate.cancel();
		} else {
			animate = new Timer();
			animate.scheduleAtFixedRate(task, 100, (long) (time * 1000));
			// System.out.println((long) (time*1000));
		}

		bToggle = !bToggle;
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
