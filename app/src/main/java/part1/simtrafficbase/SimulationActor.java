package part1.simtrafficbase;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import part1.simengineseq.AbstractSimulation;
import part1.simtrafficbase.messages.*;

import java.util.ArrayList;
import java.util.List;

public class SimulationActor extends AbstractBehavior<SimulationMessage> {
    private final AbstractSimulation sim;
    private final RoadsEnv env;
    
    private int stepsDone = 0;
    
    private final List<ActorRef<SimulationMessage>> carActors = new ArrayList<>();
    private final List<ActorRef<SimulationMessage>> tlActors = new ArrayList<>();

    public static Behavior<SimulationMessage> create(AbstractSimulation sim, RoadsEnv env) {
        return Behaviors.setup(ctx -> new SimulationActor(ctx, sim, env));
    }

    private SimulationActor(ActorContext<SimulationMessage> context, AbstractSimulation sim, RoadsEnv env) {
        super(context);
        this.sim = sim;
        this.env = env;
        System.out.println("THE ACTOR IS ALIVE");
    }

    int tlStepsReceived = 0, carStepsReceived = 0;

    @Override
    public Receive<SimulationMessage> createReceive() {
        return newReceiveBuilder()
                .onMessage(SpawnCar.class, msg -> {
//                    System.out.println("Registering car...");
                    var actor = getContext().spawn(CarActor.create(msg.car(), msg.dt()), msg.car().getId());
                    this.carActors.add(actor);
                    this.env.registerNewCar(msg.car().getId(), msg.roadNum(), msg.car().getPos());
                    return this;
                })
                .onMessage(SpawnTL.class, msg -> {
//                    System.out.println("Registering tl...");
                    var actor = getContext().spawn(TrafficLightActor.create(msg.tl(), msg.dt()), msg.tl().getId());
                    this.tlActors.add(actor);
                    this.env.registerNewTL(msg.tl().getId(), msg.roadNum(), msg.pos(), msg.tl().getState());
                    return this;
                })
                .onMessage(Begin.class, msg -> {
                    System.out.println("Beginning simulation...");
                    sim.startCycle();
                    if (this.tlActors.isEmpty()) {
                        this.carActors.forEach(act -> act.tell(new Step(getContext().getSelf())));
                    } else {
                        this.tlActors.forEach(act -> act.tell(new Step(getContext().getSelf())));
                    }
                    return this;
                })
                .onMessage(TLAction.class, msg -> {
//                    System.out.println("Received tl action");
                    this.env.updateTlState(msg.id(), msg.state());
                    if (++tlStepsReceived == tlActors.size()) {
                        tlStepsReceived = 0;
                        this.carActors.forEach(act -> act.tell(new Step(getContext().getSelf())));
                    }
                    return this;
                })
                .onMessage(GetCurrentPercept.class, msg -> {
                    msg.sender().tell(new CurrentPercept((CarPercept) this.env.getCurrentPercepts(msg.id()), getContext().getSelf().narrow()));
                    return this;
                })
                .onMessage(CarAction.class, msg -> {
//                    System.out.print(".");

                    if (msg.action() != null) {
                        double newPos = this.env.doAction(msg.id(), msg.action());
                        msg.sender().tell(new NewCarPosition(newPos));
                    }

                    if (++carStepsReceived == carActors.size()) {
                        carStepsReceived = 0;
//                        System.out.println("RECEIVED ALL CAR ACTIONS");
                        stepsDone++;
                        sim.endCycleAndWait();

                        if (stepsDone == env.getnSteps()) {
                            this.sim.stop();
                            getContext().getSelf().tell(new Stop());
                        } else {
//                            System.out.print("|");
                            sim.startCycle();
                            if (this.tlActors.isEmpty()) {
                                this.carActors.forEach(act -> act.tell(new Step(getContext().getSelf())));
                            } else {
                                this.tlActors.forEach(act -> act.tell(new Step(getContext().getSelf())));
                            }
                        }
                    }
                    return this;
                })
                .onMessage(Stop.class, msg -> {
                    System.out.println("\nStopping...");
                    tlActors.forEach(act -> act.tell(new Stop()));
                    carActors.forEach(act -> act.tell(new Stop()));
                    return Behaviors.stopped();
                })
                .build();
    }
}
