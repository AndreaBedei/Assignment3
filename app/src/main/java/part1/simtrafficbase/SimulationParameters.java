package part1.simtrafficbase;

public class SimulationParameters {
    private int nStepsPerSec,  nSteps, dt, t0;
    public SimulationParameters(int nStepsPerSec, int nSteps, int dt, int t0) {
        this.nStepsPerSec = nStepsPerSec;
        this.nSteps = nSteps;
        this.dt = dt;
        this.t0 = t0;
    }

    public int getnStepsPerSec() {
        return nStepsPerSec;
    }

    public void setnStepsPerSec(int nStepsPerSec) {
        this.nStepsPerSec = nStepsPerSec;
    }

    public int getnSteps() {
        return nSteps;
    }

    public void setnSteps(int nSteps) {
        this.nSteps = nSteps;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public int getT0() {
        return t0;
    }

    public void setT0(int t0) {
        this.t0 = t0;
    }
}