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

	private static final ActorSystem system = ActorSystem.apply("System");

	public static void main(String[] args) {
//		int nThreads = Runtime.getRuntime().availableProcessors();
//		System.out.println("Numero thread = " + nThreads);
		//var simulation = new TrafficSimulationSingleRoadTwoCars(nThreads, false);
		//var simulation = new TrafficSimulationSingleRoadSeveralCars(nThreads, false);
		//var simulation = new TrafficSimulationSingleRoadWithTrafficLightTwoCars(nThreads, false);
		//var simulation = new TrafficSimulationWithCrossRoads(nThreads, false);
		var simulation = new OurCustomTrafficSimulation(1, false);

		var simParams = new SimulationParameters(10, 100, 100, 0);

		int placeholderNumber = 5;	// FIXME:

		List<CarAgent> carAgents = new ArrayList<>();
		Map<String, ActorRef> carActors = new HashMap<>();
		for (int i = 0; i < placeholderNumber; i++) {
			String id = "car" + i;
			var agent = new CarAgentExtended(id, null, null, 0, 0 ,0, 0); // FIXME: Capire come fare
			carAgents.add(agent);
			carActors.put(id, system.actorOf(Props.create(CarActor.class, agent, simParams.dt())));
		}

		List<TrafficLight> trafficLights = new ArrayList<>();
		Map<String, ActorRef> tlActors = new HashMap<>();
		for (int i = 0; i < placeholderNumber; i++) {
			String id = "tl" + i;
			var tl = new TrafficLight(id, new P2d(0,0), TrafficLight.TrafficLightState.RED, 0, 0 ,0); // FIXME: Capire come fare
			trafficLights.add(tl);
			carActors.put(id, system.actorOf(Props.create(TrafficLightActor.class, tl, simParams.dt())));
		}

		var supervisor = system.actorOf(Props.create(SupervisorActor.class,
				simulation, null, simParams, carActors, tlActors, carAgents, trafficLights, placeholderNumber, placeholderNumber, 100));

		supervisor.tell(new Begin(), null);
		// FIXME: di sicuro questo va infilato da qualche parte nella view, però boh, ci sono tutta una serie di cose
		// FIXME: che devono essere aggiornate durante la simulazione (ES: in caso di riavvio della simulazione) che ora come ora non sono possibili
		// FIXME: bisogna inventarsi qualche modo di farle

		RoadSimStatistics stat = new RoadSimStatistics();
		RoadSimView view = new RoadSimView(simulation);
		view.display();

		simulation.addSimulationListener(stat);
		simulation.addSimulationListener(view);
	}
}
