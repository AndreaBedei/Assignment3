package part1.simtrafficbase.messages;

import akka.actor.typed.ActorRef;
import part1.simengineseq.Percept;

public record CurrentPercept(Percept currentPercept, ActorRef<CarAction> replyTo) implements SimulationMessage {}
