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

public class TrafficSimulationSingleRoadMassiveNumberOfCars extends AbstractSimulation {
	private int numCars;
	private ActorSystem<SimulationMessage> system;
	
	public TrafficSimulationSingleRoadMassiveNumberOfCars(int numCars, boolean isRandom) {
		super(isRandom);
		this.numCars = numCars;
	}
	
	public void setup() {
		RoadsEnv env = new RoadsEnv(this);
		this.setupEnvironment(env);
		
		this.setupTimings(0, 1);

		system = ActorSystem.create(SimulationActor.create(this, env), "Environment");

		Road road = env.createRoad(new P2d(0,300), new P2d(15000,300));

		Random gen = new Random(super.getRandomSeed());

		for (int i = 0; i < numCars; i++) {
			
			String carId = "car-" + i;
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
			
			/* no sync with wall-time */
		}
	}
	@Override
	public void run(int nSteps) {
		super.run(nSteps);
		system.tell(new Begin());
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
	