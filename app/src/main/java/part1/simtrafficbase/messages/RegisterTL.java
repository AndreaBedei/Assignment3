package part1.simtrafficbase.messages;

import part1.simtrafficbase.TrafficLight;

public record RegisterTL(String tlId, int roadNum, double pos, TrafficLight.TrafficLightState state) {}

