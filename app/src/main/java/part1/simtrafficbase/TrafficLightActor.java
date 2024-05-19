package part1.simtrafficbase;

import akka.actor.AbstractActor;
import part1.simtrafficbase.messages.*;

public class TrafficLightActor extends AbstractActor {
    private final TrafficLight trafficLight;
    private final int dt;

    public TrafficLightActor(TrafficLight trafficLight, int dt) {
        this.trafficLight = trafficLight;
        this.trafficLight.init();
        this.dt = dt;
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Step.class, msg -> {
                    this.trafficLight.step(this.dt);
                    sender().tell(new TLAction(this.trafficLight.getId(), this.trafficLight.getState()), getSelf());
                })
                .build();
    }
}
