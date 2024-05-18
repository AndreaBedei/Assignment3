package part1.simtrafficbase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Road {

	private double len;
	private P2d from;
	private P2d to;
	private final Map<String, TrafficLightInfo> trafficLightsInfo;

	public Road(P2d from, P2d to) {
		this.from = from;
		this.to = to;
		this.len = P2d.len(from, to);
		trafficLightsInfo = new HashMap<>();
	}
	
	public double getLen() {
		return len;
	}
	
	public P2d getFrom() {
		return from;
	}
	
	public P2d getTo() {
		return to;
	}
	
	public void addTrafficLight(TrafficLightInfo tlInfo) {
		trafficLightsInfo.put(tlInfo.getTlId(), tlInfo);
	}

	public void updateTLState(String tlId, TrafficLight.TrafficLightState tlState) {
		trafficLightsInfo.get(tlId).updateState(tlState);
	}
	
	public List<TrafficLightInfo> getTrafficLightsInfo(){
		return trafficLightsInfo.values().stream().toList();
	}
}
