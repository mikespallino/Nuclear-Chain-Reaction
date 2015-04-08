import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Popup extends JFrame {

	static final long serialVersionUID = 1L;
	public static final int WIDTH = 400, HEIGHT = 100;
	public static final String ATOMS_REACTED_LABEL = "Atoms reacted   : ";
	public static final String ENERGY_RELEASED_LABEL = "Energy released : ";
	
	public Popup(String type, JFrame parent, long atomsReacted, long totalAtoms, long energyReleased, long totalEnergy) {
		JFrame t = this;
		setTitle("Statistics for: " + type);
		JLabel atomsReactedLabel = new JLabel();
		JLabel energyReleasedLabel = new JLabel();
		JButton okayButton = new JButton("Okay");
		if(type.contains("Random Plot")) {
			atomsReactedLabel.setText(ATOMS_REACTED_LABEL + atomsReacted + " / " + totalAtoms + " = " + new DecimalFormat().format(((double)atomsReacted / totalAtoms)));
			energyReleasedLabel.setText(ENERGY_RELEASED_LABEL + energyReleased + " MeV / " + totalEnergy + " MeV = " + new DecimalFormat().format(((double)energyReleased / totalEnergy)));
		} else if(type.contains("Crystal Plot")) {
			atomsReactedLabel.setText(ATOMS_REACTED_LABEL + atomsReacted + " / " + totalAtoms + " = " + new DecimalFormat().format(((double)atomsReacted / totalAtoms)));
			energyReleasedLabel.setText(ENERGY_RELEASED_LABEL + energyReleased + " MeV / " + totalEnergy + " MeV = " + new DecimalFormat().format(((double)energyReleased / totalEnergy)));
		}
		okayButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				t.dispose();
			}
		});
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(parent);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new FlowLayout());
		add(atomsReactedLabel);
		add(energyReleasedLabel);
		add(okayButton);
		setVisible(true);
	}
	
}
