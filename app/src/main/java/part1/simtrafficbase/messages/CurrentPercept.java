package part1.simtrafficbase.messages;

import part1.simtrafficbase.CarPercept;

public record CurrentPercept(CarPercept currentPercept) implements SimulationMessage {}
