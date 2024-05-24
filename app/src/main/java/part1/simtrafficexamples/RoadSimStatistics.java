package part1.simtrafficexamples;

import java.util.List;

import part1.simengineseq.AbstractAgent;
import part1.simengineseq.AbstractEnvironment;
import part1.simengineseq.SimulationListener;
import part1.simtrafficbase.*;

/**
 * Simple class keeping track of some statistics about a traffic simulation
 * - average number of cars
 * - min speed
 * - max speed
 */
public class RoadSimStatistics implements SimulationListener {

	private double minSpeed;
	private double maxSpeed;
	
	public RoadSimStatistics() {
	}
	
	@Override
	public void notifyInit(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
	}

	@Override
	public void notifyStepDone(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
		
		maxSpeed = -1;
		minSpeed = Double.MAX_VALUE;
		for (var agent: agents) {
			CarAgent car = (CarAgent) agent;
			double currSpeed = car.getCurrentSpeed();
			if (currSpeed > maxSpeed) {
				maxSpeed = currSpeed;
			} else if (currSpeed < minSpeed) {
				minSpeed = currSpeed;
			}
		}

		//log("average speed: " + avSpeed);
	}
}
