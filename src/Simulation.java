import java.awt.Graphics2D;

public interface Simulation {
	
	public static final int MAX_ATOMS = 500;
	public static final int MIN_ATOMS = 0;
	public static final int ENERGY_PER_REACTION = 170;
	
	public void simulationSetup(boolean multipleRuns);
	public void render(Graphics2D g2d, boolean multipleRuns);
	public void update(boolean multipleRuns);
	
}
