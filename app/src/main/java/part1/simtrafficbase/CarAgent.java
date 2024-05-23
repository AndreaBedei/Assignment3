package part1.simtrafficbase;

import java.util.Optional;

import part1.simengineseq.*;

/**
 * 
 * Base class modeling the skeleton of an agent modeling a car in the traffic environment
 * 
 */
public abstract class CarAgent extends AbstractAgent {
	
	/* car model */
	protected double pos;
	protected double maxSpeed;
	protected double currentSpeed;
	protected double acceleration;
	protected double deceleration;

	/* percept and action retrieved and submitted at each step */
	protected CarPercept currentPercept;
	protected Action selectedAction;

	public CarAgent(String id, RoadsEnv env, Road road, 
			double initialPos, 
			double acc, 
			double dec,
			double vmax) {
		super(id);
		this.pos = initialPos;
		this.acceleration = acc;
		this.deceleration = dec;
		this.maxSpeed = vmax;
	}

	public void setCurrentPercept(CarPercept percept) {
		this.currentPercept = percept;
	}

	public double getPos() {
		return pos;
	}

	public void updatePos(double pos) {
		this.pos = pos;
	}

	public double getCurrentSpeed() {
		return currentSpeed;
	}
	
	protected void log(String msg) {
		System.out.println("[CAR " + this.getId() + "] " + msg);
	}

	
}
