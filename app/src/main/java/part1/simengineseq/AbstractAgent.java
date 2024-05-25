package part1.simengineseq;

/**
 * 
 * Base  class for defining types of agents taking part to the simulation
 * 
 */
public abstract class AbstractAgent {

	private String myId;
	private AbstractEnvironment env;
	
	/**
	 * Each agent has an identifier
	 * 
	 * @param id
	 */
	protected AbstractAgent(String id) {
		this.myId = id;
	}


	public abstract void decide(int dt);
	

	public String getId() {
		return myId;
	}
}
