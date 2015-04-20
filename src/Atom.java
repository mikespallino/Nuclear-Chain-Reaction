public class Atom {

	private double x, y, dx, dy;
	private boolean state;
	
	public Atom(double x, double y) {
		state = false;
		this.x = x;
		this.y = y;
		this.dx = 0;
		this.dy = 0;
	}
	
	public Atom(double x, double y, double dx, double dy) {
		state = false;
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update() {
		x += dx;
		y += dy;
	}
	
	public void trigger() {
		Neutron n1 = new Neutron(x, y);
		double dir = Math.random() * 2 * Math.PI;
		n1.setDx(1 * Math.cos(dir));
		n1.setDy(1 * Math.sin(dir));
		
		Neutron n2 = new Neutron(x, y);
		dir = Math.random() * 2 * Math.PI;
		n2.setDx(1 * Math.cos(dir));
		n2.setDy(1 * Math.sin(dir));
		
		Neutron n3 = new Neutron(x, y);
		dir = Math.random() * 2 * Math.PI;
		n3.setDx(1 * Math.cos(dir));
		n3.setDy(1 * Math.sin(dir));
		if(Main.randomSimType) {
			RandomSimulation.neutronAddQueue.add(n1);
			RandomSimulation.neutronAddQueue.add(n2);
			RandomSimulation.neutronAddQueue.add(n3);
			dir = Math.random() * 2 * Math.PI;
			RandomSimulation.barium.add(new Atom(x, y, Math.cos(dir), Math.sin(dir)));
			dir = Math.random() * 2 * Math.PI;
			RandomSimulation.krypton.add(new Atom(x, y, Math.cos(dir), Math.sin(dir)));
		} else {
			CrystalSimulation.neutronAddQueue.add(n1);
			CrystalSimulation.neutronAddQueue.add(n2);
			CrystalSimulation.neutronAddQueue.add(n3);
			dir = Math.random() * 2 * Math.PI;
			CrystalSimulation.barium.add(new Atom(x, y, Math.cos(dir), Math.sin(dir)));
			dir = Math.random() * 2 * Math.PI;
			CrystalSimulation.krypton.add(new Atom(x, y, Math.cos(dir), Math.sin(dir)));
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
