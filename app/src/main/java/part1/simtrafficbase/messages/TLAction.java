package part1.simtrafficbase.messages;

import part1.simtrafficbase.TrafficLight;

public record TLAction(TrafficLight.TrafficLightState state) implements SimulationMessage {}
