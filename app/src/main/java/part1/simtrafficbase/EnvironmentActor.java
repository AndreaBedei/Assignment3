package part1.simtrafficbase;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.typed.javadsl.Behaviors;
import part1.simengineseq.AbstractSimulation;
import part1.simtrafficbase.messages.*;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentActor extends AbstractActor { // FIXME: trasformare in AbstractBehavior<State> dove State contiene lo stato mutabile (?)
    private final AbstractSimulation sim;
    private final RoadsEnv env;
    
    private int stepsDone = 0;
    
    private final List<ActorRef> carActors = new ArrayList<>();
    private final List<ActorRef> tlActors = new ArrayList<>();

    public EnvironmentActor(AbstractSimulation sim, RoadsEnv env) {
        this.sim = sim;
        this.env = env;
        System.out.println("THE ACTOR IS ALIVE");
    }

    int tlStepsReceived = 0, carStepsReceived = 0;

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(RegisterCar.class, msg -> {
                    System.out.println("Registering car...");
                    this.carActors.add(getSender());
                    this.env.registerNewCar(msg.carId(), msg.roadNum(), msg.pos());
                })
                .match(RegisterTL.class, msg -> {
                    System.out.println("Registering tl...");
                    this.tlActors.add(getSender());
                    this.env.registerNewTL(msg.tlId(), msg.roadNum(), msg.pos(), msg.state());
                })
                .match(Begin.class, msg -> {
                    System.out.println("Beginning simulation...");
                    sim.startCycle();
                    this.tlActors.forEach(act -> act.tell(new Step(), getSelf()));
                })
                .match(TLAction.class, msg -> {
                    System.out.println("Received tl action");
                    this.env.updateTlState(msg.id(), msg.state());
                    if (++tlStepsReceived == carActors.size()) {
                        this.carActors.forEach(act -> act.tell(new Step(), getSelf()));
                    }
                })
                .match(GetCurrentPercept.class, msg -> {
                    sender().tell(new CurrentPercept((CarPercept) this.env.getCurrentPercepts(msg.id())), getSelf());
                })
                .match(CarAction.class, msg -> {
                    System.out.println("Received car action");

                    if (msg.action().isPresent()) {
                        double newPos = this.env.doAction(msg.id(), msg.action().get());
                        getSender().tell(new NewCarPosition(newPos), getSelf());
                    }

                    if (++carStepsReceived == carActors.size()) {
                        stepsDone++;
                        sim.endCycleAndWait();

                        if (stepsDone == env.getnSteps()) {
                            this.sim.stop();
                        } else {
                            sim.startCycle();
                            this.tlActors.forEach(act -> act.tell(new Step(), getSelf()));
                        }
                    }
                })
                .match(Stop.class, msg -> {
                    System.out.println("Stopping...");
                    tlActors.forEach(act -> act.tell(PoisonPill.getInstance(), getSelf()));
                    carActors.forEach(act -> act.tell(PoisonPill.getInstance(), getSelf()));
                    getContext().stop(getSelf());
                })
                .build();
    }
}
