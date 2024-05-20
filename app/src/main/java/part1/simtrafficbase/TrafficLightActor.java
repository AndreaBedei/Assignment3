package part1.simtrafficbase;

import akka.actor.AbstractActor;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import part1.simengineseq.AbstractSimulation;
import part1.simtrafficbase.messages.*;

public class TrafficLightActor extends AbstractBehavior<SimulationMessage> {
    private final TrafficLight trafficLight;
    private final int dt;

    public static Behavior<SimulationMessage> create(TrafficLight trafficLight, int dt) {
        return Behaviors.setup(ctx -> new TrafficLightActor(ctx, trafficLight, dt));
    }

    private TrafficLightActor(ActorContext<SimulationMessage> context, TrafficLight trafficLight, int dt) {
        super(context);
        this.trafficLight = trafficLight;
        this.trafficLight.init();
        this.dt = dt;
    }

    @Override
    public Receive<SimulationMessage> createReceive() {
        return newReceiveBuilder()
                .onMessage(Step.class, msg -> {
                    this.trafficLight.step(this.dt);
                    msg.sender().tell(new TLAction(this.trafficLight.getId(), this.trafficLight.getState()));
                    return this;
                })
                .build();
    }
}
