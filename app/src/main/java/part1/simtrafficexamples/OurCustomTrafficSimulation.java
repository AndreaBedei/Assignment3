package part1.simtrafficexamples;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import akka.actor.typed.ActorSystem;
import part1.simengineseq.AbstractSimulation;
import part1.simtrafficbase.*;
import part1.simtrafficbase.messages.*;

public class OurCustomTrafficSimulation extends AbstractSimulation {

    private ActorSystem<SimulationMessage> system;

    public OurCustomTrafficSimulation(boolean isRandom) {
        super(isRandom);
    }

    public void setup() {
        Random gen = new Random(super.getRandomSeed());
        RoadsEnv env = new RoadsEnv(this);
        this.setupEnvironment(env);

        this.setupTimings(0, 1);

        system = ActorSystem.create(SimulationActor.create(this, env), "Environment");

        // Road 1 creation.
        Road r1 = env.createRoad(new P2d(0,360), new P2d(1500,360));
        TrafficLight tl1 = new TrafficLight("tl1", new P2d(270,360), TrafficLight.TrafficLightState.GREEN, 75, 25, 125);
        TrafficLight tl2 = new TrafficLight("tl2", new P2d(470,360), TrafficLight.TrafficLightState.GREEN, 75, 25, 125);
        TrafficLight tl3 = new TrafficLight("tl3", new P2d(1170,360), TrafficLight.TrafficLightState.RED, 75, 25, 125);
        system.tell(new SpawnTL(tl1, 0, 270, 1));
        system.tell(new SpawnTL(tl2, 0, 470, 1));
        system.tell(new SpawnTL(tl3, 0, 1170, 1));

        // Road 2 creation.
        Road r2 = env.createRoad(new P2d(0,180), new P2d(1500,180));
        TrafficLight tl4 = new TrafficLight("tl4", new P2d(270,180), TrafficLight.TrafficLightState.GREEN, 75, 25, 125);
        TrafficLight tl5 = new TrafficLight("tl5", new P2d(470,180), TrafficLight.TrafficLightState.GREEN, 75, 25, 125);
        TrafficLight tl6 = new TrafficLight("tl6", new P2d(1170,180), TrafficLight.TrafficLightState.RED, 75, 25, 125);
        system.tell(new SpawnTL(tl4, 1, 270, 1));
        system.tell(new SpawnTL(tl5, 1, 470, 1));
        system.tell(new SpawnTL(tl6, 1, 1170, 1));

        // Road 3 creation.
        Road r3 = env.createRoad(new P2d(300,0), new P2d(300,600));
        TrafficLight tl7 = new TrafficLight("tl7", new P2d(300,330), TrafficLight.TrafficLightState.RED, 75, 25, 125);
        TrafficLight tl8 = new TrafficLight("tl8", new P2d(300,150), TrafficLight.TrafficLightState.RED, 75, 25, 125);
        system.tell(new SpawnTL(tl7, 2, 330, 1));
        system.tell(new SpawnTL(tl8, 2, 150, 1));

        // Road 4 creation.
        Road r4 = env.createRoad(new P2d(500,0), new P2d(500,600));
        TrafficLight tl9 = new TrafficLight("tl9", new P2d(500,330), TrafficLight.TrafficLightState.RED, 75, 25, 125);
        TrafficLight tl10 = new TrafficLight("tl10", new P2d(500,150), TrafficLight.TrafficLightState.RED, 75, 25, 125);
        system.tell(new SpawnTL(tl9, 3, 330, 1));
        system.tell(new SpawnTL(tl10, 3, 150, 1));

        // Road 5 creation.
        Road r5 = env.createRoad(new P2d(1200,0), new P2d(1200,600));
        TrafficLight tl11 = new TrafficLight("tl11", new P2d(1200,330), TrafficLight.TrafficLightState.GREEN, 75, 25, 125);
        TrafficLight tl12 = new TrafficLight("tl12", new P2d(1200,150), TrafficLight.TrafficLightState.GREEN, 75, 25, 125);
        system.tell(new SpawnTL(tl11, 4, 330, 1));
        system.tell(new SpawnTL(tl12, 4, 150, 1));

        // Car positioning in the right road and actor creation/registering.
        createCarsForRoad(env, r1, 0, 5, 0.1, 0.3, 5, super.mustBeRandom() ? gen : null)
                .forEach(car -> {
                    system.tell(new SpawnCar(car, 0, 1));
                });
        createCarsForRoad(env, r2, 5, 5, 0.1, 0.3, 5, super.mustBeRandom() ? gen : null)
                .forEach(car -> {
                    system.tell(new SpawnCar(car, 1, 1));
                });
        createCarsForRoad(env, r3, 10, 5, 0.1, 0.3, 5, super.mustBeRandom() ? gen : null)
                .forEach(car -> {
                    system.tell(new SpawnCar(car, 2, 1));
                });
        createCarsForRoad(env, r4, 15, 5, 0.1, 0.3, 5, super.mustBeRandom() ? gen : null)
                .forEach(car -> {
                    system.tell(new SpawnCar(car, 3, 1));
                });
        createCarsForRoad(env, r5, 20, 5, 0.1, 0.3, 5, super.mustBeRandom() ? gen : null)
                .forEach(car -> {
                    system.tell(new SpawnCar(car, 4, 1));
                });

        this.syncWithTime(25);
    }

    @Override
    public void run(int nSteps) {
        super.run(nSteps);
        System.out.println("START");
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
    protected void syncWithTime(int nCyclesPerSec) { // TODO: rimuovere?
        super.syncWithTime(nCyclesPerSec);
    }

    protected void setupEnvironment(RoadsEnv env) {
        super.setupEnvironment(env);
    }

    private List<CarAgent> createCarsForRoad(RoadsEnv env, Road r, int carIdOffset, int nCars, double acc, double dec, double vmax, Random rand){
        // Method that creates the cars for a given road.
        List<CarAgent> result = new ArrayList<>(nCars);
        boolean isRoadHorizontal = r.getTo().y() == r.getFrom().y();
        double deltaX = isRoadHorizontal ? 20 : 0;
        double deltaY = isRoadHorizontal ? 0 : 20;
        var start = r.getFrom();

        for(int i = 0; i < nCars; i++) {
            double pos = isRoadHorizontal ? start.x() + deltaX * i : start.y() + deltaY * i;

            double carAcc = acc;
            double carDec = dec;
            double carMaxSp = vmax;
            if (rand != null) {
                carAcc += rand.nextDouble() / 2;
                carDec += rand.nextDouble() / 2;
                carMaxSp += rand.nextDouble(6);
            }
            result.add(new CarAgentExtended("car-" + (carIdOffset + i), env, r, pos, carAcc, carDec, carMaxSp));
        }
        return result;
    }
}
