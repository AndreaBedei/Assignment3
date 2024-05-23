package part1.simtrafficbase;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import part1.simtrafficbase.CarAgent;
import part1.simtrafficbase.messages.*;

public class CarActor extends AbstractBehavior<SimulationMessage> {
    private final CarAgent carAgent;
    private final int dt;

    public static Behavior<SimulationMessage> create(CarAgent agent, int dt) {
        return Behaviors.setup(ctx -> new CarActor(ctx, agent, dt));
    }

    private CarActor(ActorContext<SimulationMessage> context, CarAgent agent, int dt) {
        super(context);
        this.carAgent = agent;
        this.dt = dt;
    }

    @Override
    public Receive<SimulationMessage> createReceive() {
        return newReceiveBuilder()
            .onMessage(Step.class, msg -> {
                msg.sender().tell(new GetCurrentPercept(this.carAgent.getId(), getContext().getSelf().narrow()));
                return this;
            })
            .onMessage(CurrentPercept.class, msg -> {
                this.carAgent.setCurrentPercept(msg.currentPercept());
                this.carAgent.decide(this.dt);
                msg.sender().tell(new CarAction(this.carAgent.getId(), this.carAgent.selectedAction, getContext().getSelf().narrow()));
                return this;
            })
            .onMessage(NewCarPosition.class, msg -> {
                this.carAgent.updatePos(msg.pos());
                return this;
            })
            .build();
    }
}
