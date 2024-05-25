package part1.simtrafficexamples;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import akka.actor.typed.ActorSystem;
import part1.simengineseq.AbstractSimulation;
import part1.simtrafficbase.*;
import part1.simtrafficbase.messages.Begin;
import part1.simtrafficbase.messages.SimulationMessage;
import part1.simtrafficbase.messages.SpawnCar;
import part1.simtrafficbase.messages.Stop;

/**
 * 
 * Traffic Simulation about 2 cars moving on a single road, no traffic lights
 * 
 */
public class TrafficSimulationSingleRoadTwoCars extends AbstractSimulation {

	private ActorSystem<SimulationMessage> system;

	public TrafficSimulationSingleRoadTwoCars(boolean isRandom) {
		super(isRandom);
	}
	
	public void setup() {
		
		int t0 = 0;
		int dt = 1;
		
		
		RoadsEnv env = new RoadsEnv(this);
		this.setupEnvironment(env);
		
		this.setupTimings(t0, dt);

		system = ActorSystem.create(SimulationActor.create(this, env), "Environment");

		Road r = env.createRoad(new P2d(0,300), new P2d(1500,300));


		Random gen = new Random(super.getRandomSeed());

		for (int i = 0; i < 2; i++) {

			String carId = "car-" + i;
			// double initialPos = i*30;
			double initialPos = i*10;

			double carAcceleration = 0.1;
			double carDeceleration = 0.2;
			double carMaxSpeed = 8;
			if(super.mustBeRandom()){
				carAcceleration += gen.nextDouble()/2;
				carDeceleration +=  gen.nextDouble()/2;
				carMaxSpeed += 4 + gen.nextDouble(6);
			}

			CarAgent car = new CarAgentBasic(carId, env,
					r,
					initialPos,
					carAcceleration,
					carDeceleration,
					carMaxSpeed);

			system.tell(new SpawnCar(car, 0, 1));

		}

		/* sync with wall-time: 25 steps per sec */
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
