package part1.simtrafficexamples;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import part1.simengineseq.AbstractSimulation;
import part1.simtrafficbase.CarAgent;
import part1.simtrafficbase.CarAgentExtended;
import part1.simtrafficbase.P2d;
import part1.simtrafficbase.Road;
import part1.simtrafficbase.RoadsEnv;
import part1.simtrafficbase.TrafficLight;
import part1.simtrafficbase.messages.*;
import akka.actor.typed.ActorSystem;
import part1.simtrafficbase.*;

/**
 * 
 * Traffic Simulation about 2 cars moving on a single road, with one semaphore
 * 
 */
public class TrafficSimulationSingleRoadWithTrafficLightTwoCars extends AbstractSimulation {

	private ActorSystem<SimulationMessage> system;

	public TrafficSimulationSingleRoadWithTrafficLightTwoCars(boolean isRandom) {
		super(isRandom);
	}
	
	public void setup() {

		RoadsEnv env = new RoadsEnv(this);
		this.setupEnvironment(env);
		this.setupTimings(0, 1);

		system = ActorSystem.create(SimulationActor.create(this, env), "Environment");

		Road r = env.createRoad(new P2d(0,300), new P2d(1500,300));

		TrafficLight tl = new TrafficLight("tl", new P2d(740,300), TrafficLight.TrafficLightState.GREEN, 75, 25, 100);
		system.tell(new SpawnTL(tl, 0, 330, 1));

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
		system.tell(new SpawnCar(car1, 0, 1));
		system.tell(new SpawnCar(car2, 0, 1));
		this.syncWithTime(25);
	}	
	
	@Override
	public void run(int nSteps) {
		super.run(nSteps);
		system.tell(new Begin());
	}

	@Override
	public void stop() {
		super.stop();
		system.tell(new Stop());
	}

	@Override
	protected void setupTimings(int t0, int dt) {
		super.setupTimings(t0, dt);
	}

	@Override
	protected void syncWithTime(int nCyclesPerSec) {
		super.syncWithTime(nCyclesPerSec);
	}

	protected void setupEnvironment(RoadsEnv env) {
		super.setupEnvironment(env);
	}

}
