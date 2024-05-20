package part1.simtrafficbase.messages;

import akka.actor.typed.ActorRef;
import part1.simengineseq.Action;

import java.util.Optional;

public record CarAction(String id, Optional<Action> action, ActorRef<NewCarPosition> sender) implements SimulationMessage {}
