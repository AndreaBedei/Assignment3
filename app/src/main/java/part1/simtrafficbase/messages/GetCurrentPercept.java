package part1.simtrafficbase.messages;

import akka.actor.typed.ActorRef;

public record GetCurrentPercept(String id, ActorRef<CurrentPercept> sender) implements SimulationMessage {}
