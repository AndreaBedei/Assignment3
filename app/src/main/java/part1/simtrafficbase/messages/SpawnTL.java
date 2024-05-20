package part1.simtrafficbase.messages;

import part1.simtrafficbase.TrafficLight;

public record SpawnTL(TrafficLight tl, int roadNum, double pos, int dt) implements SimulationMessage {}

