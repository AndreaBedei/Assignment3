package part1.simtrafficexamples;

public class RunTrafficSimulationMassiveTest {

	public static void main(String[] args) {		

		int numCars = 5000;
		int nSteps = 100;
		int nThreads = 20;
		
		var simulation = new TrafficSimulationSingleRoadMassiveNumberOfCars(numCars, false);
		simulation.setup();
		
		log("Running the simulation: " + numCars + " cars, for " + nSteps + " steps ...");
		
		simulation.run(nSteps);

		simulation.onSimulationCompleted(dur -> {
			System.out.println("Durata totale: " + dur + "ms");
			System.out.println("Durata media: " + dur / (double)nSteps + "ms");
		});
	}
	
	private static void log(String msg) {
		System.out.println("[ SIMULATION ] " + msg);
	}
}
