package part1.simtrafficbase.messages;

import akka.actor.typed.ActorRef;
import part1.simtrafficbase.CarPercept;

public record CurrentPercept(CarPercept currentPercept, ActorRef<CarAction> sender) implements SimulationMessage {}
