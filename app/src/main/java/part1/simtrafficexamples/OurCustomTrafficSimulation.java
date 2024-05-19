package part1.simtrafficexamples;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import part1.simengineseq.AbstractSimulation;
import part1.simtrafficbase.*;
import part1.simtrafficbase.messages.Begin;
import part1.simtrafficbase.messages.Stop;
import part1.simtrafficbase.messages.RegisterCar;
import part1.simtrafficbase.messages.RegisterTL;

public class OurCustomTrafficSimulation extends AbstractSimulation {
    private static final ActorSystem system = ActorSystem.apply("System");
    private ActorRef envActor;
    private List<ActorRef> actors;

    public OurCustomTrafficSimulation(boolean isRandom) {
        super(isRandom);
    }

    public void setup() {
        Random gen = new Random(super.getRandomSeed());
        RoadsEnv env = new RoadsEnv(this);
        this.setupEnvironment(env);

        this.setupTimings(0, 1);

        envActor = system.actorOf(Props.create(EnvironmentActor.class, this, env));

        // Road 1 creation.
        Road r1 = env.createRoad(new P2d(0,360), new P2d(1500,360));
        TrafficLight tl1 = new TrafficLight("tl1", new P2d(270,360), TrafficLight.TrafficLightState.GREEN, 75, 25, 125);
        TrafficLight tl2 = new TrafficLight("tl2", new P2d(470,360), TrafficLight.TrafficLightState.GREEN, 75, 25, 125);
        TrafficLight tl3 = new TrafficLight("tl3", new P2d(1170,360), TrafficLight.TrafficLightState.RED, 75, 25, 125);
        var act1 = system.actorOf(Props.create(TrafficLightActor.class, tl1, 1));
        var act2 = system.actorOf(Props.create(TrafficLightActor.class, tl2, 1));
        var act3 = system.actorOf(Props.create(TrafficLightActor.class, tl3, 1));
        envActor.tell(new RegisterTL(tl1.getId(), 0, 270, tl1.getState()), act1);
        envActor.tell(new RegisterTL(tl2.getId(), 0, 470, tl2.getState()), act2);
        envActor.tell(new RegisterTL(tl3.getId(), 0, 1170, tl3.getState()), act3);

        // Road 2 creation.
        Road r2 = env.createRoad(new P2d(0,180), new P2d(1500,180));
        TrafficLight tl4 = new TrafficLight("tl4", new P2d(270,180), TrafficLight.TrafficLightState.GREEN, 75, 25, 125);
        TrafficLight tl5 = new TrafficLight("tl5", new P2d(470,180), TrafficLight.TrafficLightState.GREEN, 75, 25, 125);
        TrafficLight tl6 = new TrafficLight("tl6", new P2d(1170,180), TrafficLight.TrafficLightState.RED, 75, 25, 125);
        var act4 = system.actorOf(Props.create(TrafficLightActor.class, tl4, 1));
        var act5 = system.actorOf(Props.create(TrafficLightActor.class, tl5, 1));
        var act6 = system.actorOf(Props.create(TrafficLightActor.class, tl6, 1));
        envActor.tell(new RegisterTL(tl4.getId(), 1, 270, tl4.getState()), act4);
        envActor.tell(new RegisterTL(tl5.getId(), 1, 470, tl5.getState()), act5);
        envActor.tell(new RegisterTL(tl6.getId(), 1, 1170, tl6.getState()), act6);

        // Road 3 creation.
        Road r3 = env.createRoad(new P2d(300,0), new P2d(300,600));
        TrafficLight tl7 = new TrafficLight("tl7", new P2d(300,330), TrafficLight.TrafficLightState.RED, 75, 25, 125);
        TrafficLight tl8 = new TrafficLight("tl8", new P2d(300,150), TrafficLight.TrafficLightState.RED, 75, 25, 125);
        var act7 = system.actorOf(Props.create(TrafficLightActor.class, tl7, 1));
        var act8 = system.actorOf(Props.create(TrafficLightActor.class, tl8, 1));
        envActor.tell(new RegisterTL(tl7.getId(), 2, 330, tl7.getState()), act7);
        envActor.tell(new RegisterTL(tl8.getId(), 2, 150, tl8.getState()), act8);

        // Road 4 creation.
        Road r4 = env.createRoad(new P2d(500,0), new P2d(500,600));
        TrafficLight tl9 = new TrafficLight("tl9", new P2d(500,330), TrafficLight.TrafficLightState.RED, 75, 25, 125);
        TrafficLight tl10 = new TrafficLight("tl10", new P2d(500,150), TrafficLight.TrafficLightState.RED, 75, 25, 125);
        var act9 = system.actorOf(Props.create(TrafficLightActor.class, tl9, 1));
        var act10 = system.actorOf(Props.create(TrafficLightActor.class, tl10, 1));
        envActor.tell(new RegisterTL(tl9.getId(), 3, 330, tl9.getState()), act9);
        envActor.tell(new RegisterTL(tl10.getId(), 3, 150, tl10.getState()), act10);

        // Road 5 creation.
        Road r5 = env.createRoad(new P2d(1200,0), new P2d(1200,600));
        TrafficLight tl11 = new TrafficLight("tl11", new P2d(1200,330), TrafficLight.TrafficLightState.GREEN, 75, 25, 125);
        TrafficLight tl12 = new TrafficLight("tl12", new P2d(1200,150), TrafficLight.TrafficLightState.GREEN, 75, 25, 125);
        var act11 = system.actorOf(Props.create(TrafficLightActor.class, tl11, 1));
        var act12 = system.actorOf(Props.create(TrafficLightActor.class, tl12, 1));
        envActor.tell(new RegisterTL(tl11.getId(), 4, 330, tl11.getState()), act11);
        envActor.tell(new RegisterTL(tl12.getId(), 4, 150, tl12.getState()), act12);

        // Car positioning in the right road and actor creation/registering.
        createCarsForRoad(env, r1, 0, 5, 0.1, 0.3, 5, super.mustBeRandom() ? gen : null)
                .forEach(car -> {
                    var act = system.actorOf(Props.create(CarActor.class, car, 1));
                    envActor.tell(new RegisterCar(car.getId(), 0, car.getPos()), act);
                });
        createCarsForRoad(env, r2, 5, 5, 0.1, 0.3, 5, super.mustBeRandom() ? gen : null)
                .forEach(car -> {
                    var act = system.actorOf(Props.create(CarActor.class, car, 1));
                    envActor.tell(new RegisterCar(car.getId(), 1, car.getPos()), act);
                });
        createCarsForRoad(env, r3, 10, 5, 0.1, 0.3, 5, super.mustBeRandom() ? gen : null)
                .forEach(car -> {
                    var act = system.actorOf(Props.create(CarActor.class, car, 1));
                    envActor.tell(new RegisterCar(car.getId(), 2, car.getPos()), act);
                });
        createCarsForRoad(env, r4, 15, 5, 0.1, 0.3, 5, super.mustBeRandom() ? gen : null)
                .forEach(car -> {
                    var act = system.actorOf(Props.create(CarActor.class, car, 1));
                    envActor.tell(new RegisterCar(car.getId(), 3, car.getPos()), act);
                });
        createCarsForRoad(env, r5, 20, 5, 0.1, 0.3, 5, super.mustBeRandom() ? gen : null)
                .forEach(car -> {
                    var act = system.actorOf(Props.create(CarActor.class, car, 1));
                    envActor.tell(new RegisterCar(car.getId(), 4, car.getPos()), act);
                });

        this.syncWithTime(25); // TODO: rimovibile?
    }

    @Override
    public void run(int nSteps) {
        super.run(nSteps);
        System.out.println("START");
        envActor.tell(new Begin(), null);
    }

    @Override
    public void stop() {
        super.stop();
        envActor.tell(new Stop(), null);
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
