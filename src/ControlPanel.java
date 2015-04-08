import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControlPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public static int INIT_ATOMS = Simulation.MAX_ATOMS/2;
	
	public static JButton pauseButton;
	public static JCheckBox random;
	public static JCheckBox crystal;

	public ControlPanel() {
		super();
		setLayout(new FlowLayout());
		setup();
	}

	private void setup() {
		JSlider atoms = new JSlider(JSlider.HORIZONTAL, Simulation.MIN_ATOMS, Simulation.MAX_ATOMS, INIT_ATOMS);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(0), new JLabel(new Integer(Simulation.MIN_ATOMS).toString() + " atoms"));
		labelTable.put(new Integer(Simulation.MAX_ATOMS/2), new JLabel(new Integer(Simulation.MAX_ATOMS/2).toString() + " atoms"));
		labelTable.put(new Integer(Simulation.MAX_ATOMS), new JLabel(new Integer(Simulation.MAX_ATOMS).toString() + " atoms"));
		atoms.setLabelTable(labelTable);

		atoms.setPaintLabels(true);
		atoms.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				JSlider source = (JSlider) arg0.getSource();
				if(!source.getValueIsAdjusting()) {
					INIT_ATOMS = source.getValue();
				}
			}
		});
		JButton startButton = new JButton("Start New Simulation");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(Main.randomSimType) {
					RandomSimulation.eventBus.add(new RestartSimulationEvent(this, 1, "Start"));
				} else {
					CrystalSimulation.eventBus.add(new RestartSimulationEvent(this, 1, "Start"));
				}
				Main.pause = false;
			}
		});
		pauseButton = new JButton("Unpause Simulation");
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Main.pause = !Main.pause;
				if(Main.pause) {
					pauseButton.setText("Unpause Simulation");
				} else {
					pauseButton.setText("Pause Simulation");
				}
			}
		});
		
		random = new JCheckBox("Random Sim");
		random.setSelected(true);
		crystal = new JCheckBox("Crystal Sim");
		random.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if(random.isSelected()) {
					Main.randomSimType = true;
					Main.randomSim.simulationSetup(false);
					Main.pause = true;
					crystal.setSelected(false);
					atoms.setEnabled(true);
				}
			}
		});
		crystal.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if(crystal.isSelected()) {
					Main.randomSimType = false;
					Main.crystalSim.simulationSetup(false);
					Main.pause = true;
					random.setSelected(false);
					atoms.setEnabled(false);
				}
			}
		});
		
		add(atoms);
		add(startButton);
		add(pauseButton);
		add(random);
		add(crystal);
	}
	
}
