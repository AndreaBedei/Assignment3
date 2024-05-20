package part1.simtrafficbase.messages;

import part1.simtrafficbase.CarAgent;

public record SpawnCar(CarAgent car, int roadNum, int dt) implements SimulationMessage {}
