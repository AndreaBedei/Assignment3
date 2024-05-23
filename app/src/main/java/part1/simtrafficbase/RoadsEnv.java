package part1.simtrafficbase;

import java.util.*;

import part1.simengineseq.AbstractEnvironment;
import part1.simengineseq.AbstractSimulation;
import part1.simengineseq.Action;
import part1.simengineseq.Percept;

public class RoadsEnv extends AbstractEnvironment {

	private static final int MIN_DIST_ALLOWED = 5;
	private static final int CAR_DETECTION_RANGE = 30;
	private static final int SEM_DETECTION_RANGE = 30;
	
	/* list of roads */
	private List<Road> roads;

	/**
	 * Maps traffic light ids to the road they are in.
	 */
	private HashMap<String, Road> registeredTrafficLights;
	
	/**
	 *  Cars situated in the environment.
	 */
	private HashMap<String, CarAgentInfo> registeredCars;

	private AbstractSimulation simulation;

	public RoadsEnv(AbstractSimulation simulation) {
		super("traffic-env");
		registeredCars = new HashMap<>();
		registeredTrafficLights = new HashMap<>();
		roads = new ArrayList<>();
		this.simulation = simulation;
	}
	
	@Override
	public void init() {
	}

	@Override
	public void step(int dt) {

	}
	
	public void registerNewCar(String carId, int roadNum, double pos) {
		registeredCars.put(carId, new CarAgentInfo(this.roads.get(roadNum), pos));
	}

	public Road createRoad(P2d p0, P2d p1) {
		Road r = new Road(p0, p1);
		this.roads.add(r);
		return r;
	}

	public void registerNewTL(String tlId, int roadNum, double pos, TrafficLight.TrafficLightState tlState) {
		Road r = this.roads.get(roadNum);
		r.addTrafficLight(new TrafficLightInfo(tlId, r, pos, tlState));
		registeredTrafficLights.put(tlId, r);
	}

	public void updateTlState(String tlId, TrafficLight.TrafficLightState tlState) {
		registeredTrafficLights.get(tlId).updateTLState(tlId, tlState);
	}

	@Override
	public Percept getCurrentPercepts(String agentId) {
		
		CarAgentInfo carInfo = registeredCars.get(agentId);
		double pos = carInfo.getPos();
		Road road = carInfo.getRoad();
		Optional<CarAgentInfo> nearestCar = getNearestCarInFront(road,pos, CAR_DETECTION_RANGE);
		Optional<TrafficLightInfo> nearestSem = getNearestSemaphoreInFront(road,pos, SEM_DETECTION_RANGE);
		
		return new CarPercept(pos, nearestCar, nearestSem);
	}

	private Optional<CarAgentInfo> getNearestCarInFront(Road road, double carPos, double range){
		return 
				registeredCars
				.values()
				.stream()
				.filter((carInfo) -> carInfo.getRoad() == road)
				.filter((carInfo) -> {
					double dist = carInfo.getPos() - carPos;
					return dist > 0 && dist <= range;
				})
				.min((c1, c2) -> (int) Math.round(c1.getPos() - c2.getPos()));
	}

	private Optional<TrafficLightInfo> getNearestSemaphoreInFront(Road road, double carPos, double range){
		return 
				road.getTrafficLightsInfo()
				.stream()
				.filter((TrafficLightInfo tl) -> tl.getRoadPos() > carPos)
				.min((c1, c2) -> (int) Math.round(c1.getRoadPos() - c2.getRoadPos()));
	}
	
	@Override
	public double doAction(String agentId, Action act) {
		switch (act) {
		case MoveForward mv: {
			CarAgentInfo info = registeredCars.get(agentId);
			Road road = info.getRoad();
			double pos = info.getPos();
			Optional<CarAgentInfo> nearestCar = getNearestCarInFront(road, pos, CAR_DETECTION_RANGE);

			if (nearestCar.isPresent()) {
				double dist = nearestCar.get().getPos() - pos;
				if (dist > mv.distance() + MIN_DIST_ALLOWED) {
					info.updatePos(pos + mv.distance());
				}
			} else {
				info.updatePos(pos + mv.distance());
			}

			if (info.getPos() > road.getLen()) { // Account for updated position.
				info.updatePos(0);
			}

			return info.getPos();
		}
		default: break;
		}
		return 0;
	}
	
	
	public List<CarAgentInfo> getAgentInfo(){
		return this.registeredCars.values().stream().toList();
	}

	public int numOfCars(){
		return this.registeredCars.size();
	}

	public List<Road> getRoads(){
		return roads;
	}
	
	public List<TrafficLightInfo> getTrafficLightsInfo(){
		return this.getRoads()
				.stream()
				.flatMap(r -> r.getTrafficLightsInfo().stream())
				.toList();
	}

	public int numOfTrafficLights(){
		return this.getRoads()
				.stream()
				.mapToInt(r -> r.getTrafficLightsInfo().size())
				.sum();
	}
}
