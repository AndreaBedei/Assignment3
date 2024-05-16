package part1.simtrafficbase.messages;

import akka.actor.typed.ActorRef;
import part1.simengineseq.Action;

public record CarAction(Action action) implements SimulationMessage {}
