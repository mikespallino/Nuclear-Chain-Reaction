import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class CrystalSimulation implements Simulation {

	private Atom[] atoms = new Atom[144];
	private ArrayList<Neutron> neutrons = new ArrayList<Neutron>();
	public static ArrayList<Neutron> neutronAddQueue = new ArrayList<Neutron>();
	public static ArrayList<Neutron> neutronRemoveQueue = new ArrayList<Neutron>();
	
	public static ArrayList<Atom> krypton = new ArrayList<Atom>();
	public static ArrayList<Atom> barium = new ArrayList<Atom>();
	
	private boolean finished = false;
	private boolean firstRunNotMultiple;
	
	public long energyReleased = 0;
	public int atomsReacted = 0;

	public static ArrayList<RestartSimulationEvent> eventBus = new ArrayList<RestartSimulationEvent>();
	
	@Override
	public void simulationSetup(boolean multipleRuns) {
		if(multipleRuns) {
			firstRunNotMultiple = false;
		} else {
			firstRunNotMultiple = true;
		}
		finished = false;
		energyReleased = 0;
		atomsReacted = 0;
		atoms = new Atom[144];
		double offset = (2 * Math.PI) / (double)12;
		double SCALE_X = 350;
		double SCALE_Y = 50;
		int index = 0;
		for(double i = 0; i < (2 * Math.PI); i+=offset) {
			for(double j = 0; j < (2 * Math.PI); j += offset) {
				atoms[index] = new Atom((Math.cos(i+j) * 25) + SCALE_X, (Math.sin(i+j) * 25) + SCALE_Y);
				index++;
			}
			SCALE_X += 60 * Math.cos(i);
			SCALE_Y += 60 * Math.sin(i);
		}
		neutrons.clear();
		neutronAddQueue.clear();
		neutronRemoveQueue.clear();
		neutrons.add(new Neutron(Main.WIDTH/2,0));
		neutrons.get(0).setDx(0);
		neutrons.get(0).setDy(1);
		barium.clear();
		krypton.clear();
		//neutrons.add(new Neutron(Math.random() * (WIDTH - 20), Math.random() * (HEIGHT - 30)));
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
		for(Atom a : barium) {
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
			} else if(n.getY() < 0 || n.getY() > Main.HEIGHT) {
				neutronRemoveQueue.add(n);
			}
		}
		for(int i = 0; i < krypton.size(); i++) {
			Atom a = krypton.get(i);
			a.update();
			if(a.getX() < 0 || a.getX() > Main.WIDTH - 20) {
				krypton.remove(i);
			} else if(a.getY() < 0 || a.getY() > Main.HEIGHT - 110) {
				krypton.remove(i);
			}
		}
		for(int i = 0; i < barium.size(); i++) {
			Atom a = barium.get(i);
			a.update();
			if(a.getX() < 0 || a.getX() > Main.WIDTH - 20) {
				barium.remove(i);
			} else if(a.getY() < 0 || a.getY() > Main.HEIGHT - 110) {
				barium.remove(i);
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
