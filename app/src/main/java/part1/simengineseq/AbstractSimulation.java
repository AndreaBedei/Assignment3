package part1.simengineseq;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


/**
 * Base class for defining concrete simulations
 *  
 */
public abstract class AbstractSimulation {

	/* environment of the simulation */
	private AbstractEnvironment env;

	/* list of the agents */
	private List<AbstractAgent> agents; // Non usato.
		
	/* simulation listeners */
	private List<SimulationListener> listeners;

	/* logical time step */
	private int dt;
	
	/* initial logical time */
	private int t0;

	private int t;

	/* in the case of sync with wall-time */
	private boolean toBeInSyncWithWallTime;
	private int nStepsPerSec;
	
	/* for time statistics*/
	private long currentWallTime;
	private long startWallTime;
	private long endWallTime;


	private boolean isRandom;	// Specifies if the simulation should include randomness.

	private final int randomSeed = 1;	// Constant random seed for reproducibility.
	private Consumer<Long> onComplete;

	protected AbstractSimulation(boolean isRandom) {
		agents = new ArrayList<AbstractAgent>();
		listeners = new ArrayList<SimulationListener>();
		toBeInSyncWithWallTime = false;
		this.isRandom = isRandom;
	}
	
	/**
	 * 
	 * Method used to configure the simulation, specifying env and agents
	 * 
	 */
	public abstract void setup();
	
	/**
	 * Method running the simulation for a number of steps,
	 * using a sequential approach
	 * 
	 * @param numSteps
	 */
	public void run(int numSteps) {
		startWallTime = System.currentTimeMillis();

		/* initialize the env and the agents inside */
		this.t = t0;

		env.setnSteps(numSteps);
		env.init();

		this.notifyReset(t, agents, env);
	}
	
	public long getSimulationDuration() {
		return endWallTime - startWallTime;
	}
	
	/* methods for configuring the simulation */
	
	protected void setupTimings(int t0, int dt) {
		this.dt = dt;
		this.t0 = t0;
		this.env.setDt(dt);
	}
	
	protected void syncWithTime(int nCyclesPerSec) {
		this.toBeInSyncWithWallTime = true;
		this.nStepsPerSec = nCyclesPerSec;
		this.env.setCyclesPerSec(nCyclesPerSec);
	}
		
	protected void setupEnvironment(AbstractEnvironment env) {
		this.env = env;
	}

	
	public void addSimulationListener(SimulationListener l) {
		this.listeners.add(l);
	}
	
	private void notifyReset(int t0, List<AbstractAgent> agents, AbstractEnvironment env) {
		for (var l: listeners) {
			l.notifyInit(t0, agents, env);
		}
	}

	private void notifyNewStep(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
		for (var l: listeners) {
			l.notifyStepDone(t, agents, env);
		}
	}

	public void stop(){
		this.endWallTime = System.currentTimeMillis();
		if(onComplete != null){
			this.onComplete.accept(this.getSimulationDuration());
		}
	}

	public void notifySimulationStep(int t){
		notifyNewStep(t, agents, env);
	}	// Useful for GUI.

	public boolean mustBeRandom(){
		return this.isRandom;
	}

	public int getRandomSeed(){
		return this.randomSeed;
	}

	public void startCycle() {
		currentWallTime = System.currentTimeMillis();
	}

	public void endCycleAndWait() {
		this.t += this.dt;
		notifySimulationStep(this.t);
		if(nStepsPerSec>0 ){
			syncWithWallTime();
		}
	}

	private void syncWithWallTime() {
		try {
			long newWallTime = System.currentTimeMillis();
			long delay = 1000 / this.nStepsPerSec;
			long wallTimeDT = newWallTime - this.currentWallTime;
			if (wallTimeDT < delay) {
				Thread.sleep(delay - wallTimeDT);
			}
		} catch (Exception ex) {}
	}

	// Ci serve come callback quando la simulazione finisce per far vedere i dati della simulazione.
	public void onSimulationCompleted(Consumer<Long> onComplete) {
		this.onComplete = onComplete;
	}
}
