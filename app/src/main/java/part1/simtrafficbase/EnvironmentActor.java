package part1.simtrafficbase;

import akka.actor.AbstractActor;
import part1.simtrafficbase.messages.*;

public class EnvironmentActor extends AbstractActor {
    private final RoadsEnv env;

    private

    public EnvironmentActor(RoadsEnv env) {
        this.env = env;
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
                .match(RegisterCar.class, msg -> {
                    this.env.registerNewCar(msg.carId(), msg.roadNum(), msg.pos());
                })
                .match(RegisterTL.class, msg -> {
                    this.env.registerNewTL(msg.tlId(), msg.roadNum(), msg.pos(), msg.state());
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
