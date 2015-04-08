public class Neutron {
	
	private double x, y, dx, dy;
	
	public Neutron(double x, double y) {
		this.setX(x);
		this.setY(y);
		setDx(0);
		setDy(0);
	}
	
	public void update(Atom[] atoms) {
		x += dx;
		y += dy;
		for(Atom a: atoms) {
			if(x > a.getX() && x < a.getX()+10 && y > a.getY() && y < a.getY()+10) {
				if(!a.getState()) {
					a.trigger();
					if(Main.randomSimType) {
						RandomSimulation.neutronRemoveQueue.add(this);
					} else {
						CrystalSimulation.neutronRemoveQueue.add(this);
					}
				}
			}
		}
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
