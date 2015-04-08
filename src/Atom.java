public class Atom {

	private double x, y, dx, dy;
	private boolean state;
	
	public Atom(double x, double y) {
		state = false;
		this.setX(x);
		this.setY(y);
		setDx(0);
		setDy(0);
	}
	
	public void update() {
		x += dx;
		y += dy;
	}
	
	public void trigger() {
		Neutron n1 = new Neutron(x -.5, y-.5);
		double dir = Math.random() * 2 * Math.PI;
		n1.setDx(1 * Math.cos(dir));
		n1.setDy(Math.sin(dir));
		
		Neutron n2 = new Neutron(x - .5, y -.5);
		dir = Math.random() * 2 * Math.PI;
		n2.setDx(1 * Math.cos(dir));
		n2.setDy(Math.sin(dir));
		
		Neutron n3 = new Neutron(x - .5, y -.5);
		dir = Math.random() * 2 * Math.PI;
		n3.setDx(1 * Math.cos(dir));
		n3.setDy(Math.sin(dir));
		if(Main.randomSimType) {
			RandomSimulation.neutronAddQueue.add(n1);
			RandomSimulation.neutronAddQueue.add(n2);
			RandomSimulation.neutronAddQueue.add(n3);
		} else {
			CrystalSimulation.neutronAddQueue.add(n1);
			CrystalSimulation.neutronAddQueue.add(n2);
			CrystalSimulation.neutronAddQueue.add(n3);
		}
		state = true;
	}
	
	public boolean getState() {
		return state;
	}
	
	public void setState(boolean val) {
		state = val;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getDx() {
		return dx;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public double getDy() {
		return dy;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}
	
}
