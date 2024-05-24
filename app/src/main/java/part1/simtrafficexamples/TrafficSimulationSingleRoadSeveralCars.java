package part1.simtrafficexamples;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import part1.simengineseq.AbstractSimulation;
import part1.simtrafficbase.CarAgent;
import part1.simtrafficbase.CarAgentBasic;
import part1.simtrafficbase.P2d;
import part1.simtrafficbase.Road;
import part1.simtrafficbase.RoadsEnv;
import part1.simtrafficbase.messages.Begin;
import part1.simtrafficbase.messages.SimulationMessage;
import part1.simtrafficbase.messages.SpawnCar;
import akka.actor.typed.ActorSystem;
import part1.simtrafficbase.*;
import part1.simtrafficbase.messages.Stop;

/**
 * 
 * Traffic Simulation about a number of cars 
 * moving on a single road, no traffic lights
 * 
 */
public class TrafficSimulationSingleRoadSeveralCars extends AbstractSimulation {

	private ActorSystem<SimulationMessage> system;

	public TrafficSimulationSingleRoadSeveralCars(boolean isRandom) {
		super(isRandom);
	}

	// TODO: resto delle simulazioni ad attori

	public void setup() {

		RoadsEnv env = new RoadsEnv(this);
		this.setupEnvironment(env);
		this.setupTimings(0, 1);
		
		Road road = env.createRoad(new P2d(0,300), new P2d(1500,300));

		int nCars = 30;

		system = ActorSystem.create(SimulationActor.create(this, env), "Environment");

		Random gen = new Random(super.getRandomSeed());

		// FIXME: forse tutti gli attori andrebbero ricreati nel setup della simulazione?

		for (int i = 0; i < nCars; i++) {
			
			String carId = "car-" + i;
			// double initialPos = i*30;
			double initialPos = i*10;

			double carAcceleration = 1;
			double carDeceleration = 0.3;
			double carMaxSpeed = 7;
			if(super.mustBeRandom()){
				carAcceleration += gen.nextDouble()/2;
				carDeceleration +=  gen.nextDouble()/2;
				carMaxSpeed += 4 + gen.nextDouble(6);
			}
						
			CarAgent car = new CarAgentBasic(carId, env, 
									road,
									initialPos, 
									carAcceleration, 
									carDeceleration,
									carMaxSpeed);

			system.tell(new SpawnCar(car, 0, 1));
		}

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
	