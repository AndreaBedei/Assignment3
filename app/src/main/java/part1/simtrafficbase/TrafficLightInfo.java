package part1.simtrafficbase;

public class TrafficLightInfo {
    private final String tlId;
    private final Road road;
    private final double roadPos;
    private TrafficLight.TrafficLightState tlState;

    public TrafficLightInfo(String tlId, Road road, double roadPos, TrafficLight.TrafficLightState tlState) {
        this.tlId = tlId;
        this.road = road;
        this.roadPos = roadPos;
        this.tlState = tlState;
    }

    public String getTlId() {
        return tlId;
    }

    public Road getRoad() {
        return road;
    }

    public double getRoadPos() {
        return roadPos;
    }

    public TrafficLight.TrafficLightState getState() {
        return tlState;
    }

    public void updateState(TrafficLight.TrafficLightState state) {
        this.tlState = state;
    }
}
