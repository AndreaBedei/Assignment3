package part1.simtrafficbase.messages;

import akka.actor.typed.ActorRef;

public record GetCurrentPercept(ActorRef<CurrentPercept> replyTo) implements SimulationMessage {}
