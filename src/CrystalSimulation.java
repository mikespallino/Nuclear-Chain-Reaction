import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class CrystalSimulation implements Simulation {

	private Atom[] atoms = new Atom[13];
	private ArrayList<Neutron> neutrons = new ArrayList<Neutron>();
	public static ArrayList<Neutron> neutronAddQueue = new ArrayList<Neutron>();
	public static ArrayList<Neutron> neutronRemoveQueue = new ArrayList<Neutron>();
	
	public static ArrayList<Atom> krypton = new ArrayList<Atom>();
	public static ArrayList<Atom> bromine = new ArrayList<Atom>();
	
	private boolean finished = false;
	
	public long energyReleased = 0;
	public int atomsReacted = 0;

	public static ArrayList<RestartSimulationEvent> eventBus = new ArrayList<RestartSimulationEvent>();
	
	@Override
	public void simulationSetup(boolean multipleRuns) {
		finished = false;
		energyReleased = 0;
		atomsReacted = 0;
		atoms = new Atom[13];
		atoms[0] = new Atom(50, 50);
		atoms[1] = new Atom(65, 53);
		atoms[2] = new Atom(80, 47);
		atoms[3] = new Atom(95, 49);
		atoms[4] = new Atom(63, 65);
		atoms[5] = new Atom(75, 68);
		atoms[6] = new Atom(88, 71);
		atoms[7] = new Atom(100, 65);
		atoms[8] = new Atom(45, 71);
		atoms[9] = new Atom(60, 90);
		atoms[10] = new Atom(72, 100);
		atoms[11] = new Atom(85, 85);
		atoms[12] = new Atom(98, 90);
		neutrons.clear();
		neutronAddQueue.clear();
		neutronRemoveQueue.clear();
		bromine.clear();
		krypton.clear();
		//neutrons.add(new Neutron(Math.random() * (WIDTH - 20), Math.random() * (HEIGHT - 30)));
		neutrons.add(new Neutron(0,0));
		neutrons.get(0).setDx(1);
		neutrons.get(0).setDy(1);
		eventBus.clear();
	}

	@Override
	public void render(Graphics2D g2d, boolean multipleRuns) {
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
		g2d.setColor(Color.BLACK);
		for(Neutron n: neutrons) {
			g2d.fillOval((int) n.getX(), (int) n.getY(), 2, 2);
		}
		g2d.setColor(Color.GREEN);
		for(Atom a : atoms) {
			if(!a.getState()) {
				g2d.fillOval((int) a.getX(), (int) a.getY(), 10, 10);
			}
		}
		g2d.setColor(Color.ORANGE);
		for(Atom a : krypton) {
			g2d.fillOval((int) a.getX(), (int) a.getY(), 5, 5);
		}
		g2d.setColor(Color.BLUE);
		for(Atom a : bromine) {
			g2d.fillOval((int) a.getX(), (int) a.getY(), 5, 5);
		}
		if(neutrons.size() ==0) {
			for(Atom a: atoms) {
				if(a.getState()) {
					atomsReacted++;
					energyReleased += ENERGY_PER_REACTION;
				}
			}
			finished = true;
			g2d.setColor(Color.BLACK);
			g2d.drawString("SIMULATION END.", 340, 210);
			Main.pause = true;
			ControlPanel.pauseButton.setText("Unpause Simulation");
			if(!multipleRuns) {
				new Popup("Crystal Plot", Main.frame, atomsReacted, atoms.length, energyReleased, atoms.length * ENERGY_PER_REACTION);
			}
		}
	}

	@Override
	public void update(boolean multipleRuns) {
		for(Neutron n: neutrons){
			n.update(atoms);
			if(n.getX() < 0 || n.getX() > Main.WIDTH) {
				neutronRemoveQueue.add(n);
			}
			if(n.getY() < 0 || n.getY() > Main.HEIGHT) {
				neutronRemoveQueue.add(n);
			}
		}
		for(int i = 0; i < krypton.size(); i++) {
			Atom a = krypton.get(i);
			a.update();
			if(a.getX() < 0 || a.getX() > Main.WIDTH - 20) {
				krypton.remove(i);
			}
			if(a.getY() < 0 || a.getY() > Main.HEIGHT - 110) {
				krypton.remove(i);
			}
		}
		for(int i = 0; i < bromine.size(); i++) {
			Atom a = bromine.get(i);
			a.update();
			if(a.getX() < 0 || a.getX() > Main.WIDTH - 20) {
				bromine.remove(i);
			}
			if(a.getY() < 0 || a.getY() > Main.HEIGHT - 110) {
				bromine.remove(i);
			}
		}
		neutrons.addAll(neutronAddQueue);
		neutronAddQueue.clear();
		neutrons.removeAll(neutronRemoveQueue);
		neutronRemoveQueue.clear();
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

}
