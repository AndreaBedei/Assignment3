package part1.simtrafficexamples;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import static akka.pattern.Patterns.ask;

import part1.simtrafficbase.*;
import part1.simtrafficbase.messages.Begin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Main class to create and run a simulation
 * 
 */
public class RunTrafficSimulation {

	public static void main(String[] args) {
		//var simulation = new TrafficSimulationSingleRoadTwoCars(false);
		//var simulation = new TrafficSimulationSingleRoadSeveralCars(false);
		//var simulation = new TrafficSimulationSingleRoadWithTrafficLightTwoCars(true);
		//var simulation = new TrafficSimulationWithCrossRoads(false);
		var simulation = new OurCustomTrafficSimulation(false);

		RoadSimStatistics stat = new RoadSimStatistics();
		RoadSimView view = new RoadSimView(simulation);
		view.display();

		simulation.addSimulationListener(stat);
		simulation.addSimulationListener(view);
	}
}
