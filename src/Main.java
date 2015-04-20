import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Main extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private static final String TITLE = "Nuclear Reaction Demo | ";
	public static final int WIDTH = 850, HEIGHT = 450;

	public static int ITERATIONS = 20;
	
	private boolean running = false;
	public static boolean pause = true;
	public static boolean randomSimType = true;
	private Thread thread;
	
	public static JFrame frame;
	private ControlPanel panel;
	public static ArrayList<FinalStatisticsEvent> eventBus = new ArrayList<FinalStatisticsEvent>();
	
	public static RandomSimulation randomSim = new RandomSimulation();
	public static CrystalSimulation crystalSim = new CrystalSimulation();
	
	public static void main(String[] args) {
		Main m = new Main();
		m.setup();
		m.start();
	}
	
	private synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.run();
	}
	
	private synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void setup() {
		frame = new JFrame(TITLE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		JMenuBar menu = new JMenuBar();
		JMenu simulation = new JMenu("Statistics");
		JMenuItem randomPlot = new JMenuItem("Random Plot Statistics");
		JMenuItem crystalPlot = new JMenuItem("Crystal Plot Statistics");
		randomPlot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ControlPanel.random.setSelected(true);
				randomSim.simulationSetup(true);
				long atomsReacted = 0;
				long energyReleased = 0;
				for(int i = 0; i < ITERATIONS; i++) {
					while (!randomSim.isFinished()) {
						randomSim.update(true);
						render(true);
						//randomSim.render((Graphics2D)getBufferStrategy().getDrawGraphics(), true);
					}
					atomsReacted += randomSim.atomsReacted;
					energyReleased += randomSim.energyReleased;
					randomSim.simulationSetup(true);
				}
				new Popup("Random Plot for " + ITERATIONS + " Iterations", Main.frame, atomsReacted, ControlPanel.INIT_ATOMS * ITERATIONS, energyReleased, ControlPanel.INIT_ATOMS * ITERATIONS * Simulation.ENERGY_PER_REACTION);				
			}
		});
		crystalPlot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ControlPanel.crystal.setSelected(true);
				crystalSim.simulationSetup(true);
				long atomsReacted = 0;
				long energyReleased = 0;
				for(int i = 0; i < ITERATIONS; i++) {
					while (!crystalSim.isFinished()) {
						crystalSim.update(true);
						render(true);
						//crystalSim.render((Graphics2D)getBufferStrategy().getDrawGraphics(), true);
					}
					atomsReacted += crystalSim.atomsReacted;
					energyReleased += crystalSim.energyReleased;
					crystalSim.simulationSetup(true);
				}
				new Popup("Crystal Plot for " + ITERATIONS + " Iterations", Main.frame, atomsReacted, 144 * ITERATIONS, energyReleased, 144 * ITERATIONS * Simulation.ENERGY_PER_REACTION);				
			}
		});
		simulation.add(randomPlot);
		simulation.add(crystalPlot);
		menu.add(simulation);
		menu.setVisible(true);
		frame.getContentPane().add(menu, BorderLayout.NORTH);
		panel = new ControlPanel();
		frame.getContentPane().add(this, BorderLayout.CENTER);
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		frame.setVisible(true);
		running = true;
		RandomSimulation.eventBus.add(new RestartSimulationEvent(this, 0, "Start"));
		randomSim.simulationSetup(false);
		createBufferStrategy(3);
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		long now;
		double delta = 0.0;
		double ns = 1000000000.0/60.0;
		int frames = 0;
		int updates = 0;
		long timer = System.currentTimeMillis();
		while(running) {
			now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				delta--;
				if(randomSimType) {
					if(RandomSimulation.eventBus.size() == 0) {
						if(pause == false) {
							randomSim.update(false);
							updates++;
						}
					} else {
						randomSim.simulationSetup(false);
					}
				} else {
					if(CrystalSimulation.eventBus.size() == 0) {
						if(pause == false) {
							crystalSim.update(false);
							updates++;
						}
					} else {
						crystalSim.simulationSetup(false);
					}
				}
			}
			if(RandomSimulation.eventBus.size() == 0 || CrystalSimulation.eventBus.size() == 0) {
				if(!pause) {
					render(false);
					frames++;
				}
			}
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle(TITLE + updates + " ups " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}

	private void render(boolean multipleRuns) {
		Graphics2D g2d = (Graphics2D) getBufferStrategy().getDrawGraphics();
		if(randomSimType && !randomSim.isFinished()) {
			randomSim.render(g2d, multipleRuns);
		} else if (!randomSimType && !crystalSim.isFinished()) {
			crystalSim.render(g2d, multipleRuns);
		}
		g2d.dispose();
		getBufferStrategy().show();
	}
	
}
