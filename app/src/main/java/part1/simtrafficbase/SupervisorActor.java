package part1.simtrafficbase;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import part1.simengineseq.AbstractAgent;
import part1.simengineseq.AbstractSimulation;
import part1.simengineseq.Action;
import part1.simtrafficbase.messages.*;

import java.util.*;
import java.util.stream.Collectors;

public class SupervisorActor extends AbstractActor {
    private final AbstractSimulation simulation;
    private final RoadsEnv env;
    private final SimulationParameters parameters;
    private final List<ActorRef> carActors, tlActors;
    private final Map<String, CarAgent> carAgents;
    private final Map<String, TrafficLight> trafficLights;
    private final int nCars;
    private final int nTrafficLights;
    private final int dt;

    private Map<String, Optional<Action>> stepActions;
    private Map<String, TrafficLight.TrafficLightState> stepTLActions;

    public SupervisorActor(AbstractSimulation sim, RoadsEnv env, SimulationParameters params,
                           List<ActorRef> carActors, List<ActorRef> tlActors, List<CarAgent> carAgents, List<TrafficLight> trafficLights,
                           int nCars, int nTrafficLights, int dt) {
        this.simulation = sim;
        this.env = env;
        this.parameters = params;
        this.carActors = carActors;
        this.tlActors = tlActors;
        this.carAgents = carAgents.stream().collect(Collectors.toMap(CarAgent::getId, c -> c));
        this.trafficLights = trafficLights.stream().collect(Collectors.toMap(TrafficLight::getId, tl -> tl));
        this.nCars = nCars;
        this.nTrafficLights = nTrafficLights;
        this.dt = dt;

        this.stepActions = new HashMap<>();
        this.stepTLActions = new HashMap<>();
    }

    private void performTLStep() {

    }
    private void performCarStep() {

    }

    private void initNextStep() {

    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Begin.class, msg -> {
                    this.tlActors.forEach(act -> act.tell(new Step(), getSelf()));
                })
                .match(TLAction.class, msg -> {
                    this.stepTLActions.put(msg.id(), msg.state());
                    if (this.stepTLActions.size() == this.trafficLights.size()) {
                        performTLStep();
                        this.carActors.forEach(act -> act.tell(new Step(), getSelf()));
                    }
                })
                .match(GetCurrentPercept.class, msg -> {
                    sender().tell(new CurrentPercept(this.env.getCurrentPercepts(msg.id())), getSelf());
                })
                .match(CarAction.class, msg -> {
                    this.stepActions.put(msg.id(), msg.action());
                    if (this.stepActions.size() == this.carAgents.size()) {
                        performCarStep();
                        // Sincronizzazione tempo di ciclo
                        this.tlActors.forEach(act -> act.tell(new Step(), getSelf()));
                    }
                })
                .build();
    }
}
