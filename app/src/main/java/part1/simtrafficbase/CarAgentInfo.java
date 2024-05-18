package part1.simtrafficbase;

public class CarAgentInfo {
	private double pos;
	private Road road;
	
	public CarAgentInfo(Road road, double pos) {
		this.road = road;
		this.pos = pos;
	}
	
	public double getPos() {
		return pos;
	}
	
	public void updatePos(double pos) {
		this.pos = pos;
	}

	public Road getRoad() {
		return road;
	}
}
