package part1.simtrafficbase;

import akka.actor.AbstractActor;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.Behaviors;
import part1.simtrafficbase.CarAgent;
import part1.simtrafficbase.messages.*;

public class CarActor extends AbstractActor {
    private final CarAgent carAgent;
    private final int dt;

    public CarActor(CarAgent agent, int dt) {
        this.carAgent = agent;
        this.dt = dt;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(Step.class, msg -> {
                sender().tell(new GetCurrentPercept(this.carAgent.getId()), getSelf());
            })
            .match(CurrentPercept.class, msg -> {
                this.carAgent.decide(this.dt);
                sender().tell(new CarAction(this.carAgent.getId(), this.carAgent.selectedAction), getSelf());
            })
            .build();
    }
}
