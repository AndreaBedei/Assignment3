package part1.simtrafficbase.messages;

import akka.actor.ActorRef;

public record RegisterCar(String carId, int roadNum, double pos) {}
