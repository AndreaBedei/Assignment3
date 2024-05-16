package part1.simtrafficexamples;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import part1.simengineseq.AbstractSimulation;
import part1.simtrafficbase.CarAgent;
import part1.simtrafficbase.CarAgentExtended;
import part1.simtrafficbase.SimThreadsSupervisor;
import part1.simtrafficbase.P2d;
import part1.simtrafficbase.Road;
import part1.simtrafficbase.RoadsEnv;
import part1.simtrafficbase.TrafficLight;

/**
 * 
 * Traffic Simulation about 2 cars moving on a single road, with one semaphore
 * 
 */
public class TrafficSimulationSingleRoadWithTrafficLightTwoCars extends AbstractSimulation {

	private final SimThreadsSupervisor supervisor;

	public TrafficSimulationSingleRoadWithTrafficLightTwoCars(int nThreads, boolean isRandom) {
		super(isRandom);
		this.supervisor = new SimThreadsSupervisor(nThreads, this);

	}
	
	public void setup() {

		RoadsEnv env = new RoadsEnv(this);
		this.setupEnvironment(env);
		this.setupTimings(0, 1);
		

				
		Road r = env.createRoad(new P2d(0,300), new P2d(1500,300));

		TrafficLight tl = env.createTrafficLight(new P2d(740,300), TrafficLight.TrafficLightState.GREEN, 75, 25, 100);
		r.addTrafficLight(tl, 740);
		
		List<CarAgent> cars = new ArrayList<>();
		List<TrafficLight> lights = new ArrayList<>();

		CarAgent car1;
		CarAgent car2;

		if(super.mustBeRandom()){
			Random gen = new Random(super.getRandomSeed());
			double carAcceleration = 0.1 + gen.nextDouble()/2;
			double carDeceleration =  0.3 + gen.nextDouble()/2;
			double carMaxSpeed = 5 + gen.nextDouble(6);
			car1 = new CarAgentExtended("car-1", env, r, 0, carAcceleration, carDeceleration, carMaxSpeed);
			carAcceleration = 0.1 + gen.nextDouble()/2;
			carDeceleration =  0.3 + gen.nextDouble()/2;
			carMaxSpeed = 5 + gen.nextDouble();
			car2 = new CarAgentExtended("car-2", env, r, 100, carAcceleration, carDeceleration, carMaxSpeed);
		} else{
			// No random in this simulation.
			car1 = new CarAgentExtended("car-1", env, r, 0, 0.1, 0.3, 6);

			car2 = new CarAgentExtended("car-2", env, r, 100, 0.1, 0.3, 5);
		}

		this.addAgent(car1);
		this.addAgent(car2);

		cars.add(car1);
		cars.add(car2);

		lights.add(tl);

		supervisor.createCars(cars);
		supervisor.createTrafficLights(lights);

		this.syncWithTime(25);
	}	
	
	@Override
	public void run(int nSteps) {
		this.supervisor.setSteps(nSteps);
		super.run(nSteps);
		this.supervisor.runAllThreads();
	}

	@Override
	protected void setupTimings(int t0, int dt) {
		super.setupTimings(t0, dt);
		this.supervisor.setTimings(t0, dt);
	}

	@Override
	protected void syncWithTime(int nCyclesPerSec) {
		super.syncWithTime(nCyclesPerSec);
		this.supervisor.setStepsPerSec(nCyclesPerSec);
	}

	protected void setupEnvironment(RoadsEnv env) {
		super.setupEnvironment(env);
		this.supervisor.setEnvironment(env);
	}

}
