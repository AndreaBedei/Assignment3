package part1.simtrafficbase;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import part1.simtrafficbase.messages.*;

// E' l'attore che gestisce i messaggi, in base al messaggio manipola un CarAgent (singola macchina).
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
                // Quando l'attore della macchinina riceve il messaggio Step, richiede all'ambiente la percezione.
                msg.sender().tell(new GetCurrentPercept(this.carAgent.getId(), getContext().getSelf().narrow()));
                return this;
            })
            .onMessage(CurrentPercept.class, msg -> {
                this.carAgent.setCurrentPercept(msg.currentPercept());
                this.carAgent.decide(this.dt);
                // Risponde con l'azione che vuole fare in base alla percezione ricevuta.
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
