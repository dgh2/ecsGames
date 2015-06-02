package pong.components;

import ecs.Component;

public class Velocity implements Component {
    protected double magnitude, direction, decayRate;
    public Velocity(double magnitude, double direction, double decayRate) {
        this.magnitude = magnitude;
        this.direction = direction;
        this.decayRate = decayRate;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double getDecayRate() {
        return decayRate;
    }

    public void setDecayRate(double decayRate) {
        this.decayRate = decayRate;
    }

    @Override
    public String toString() {
        return "(magnitude " + magnitude + " @ " + direction + " degrees, decaying at a rate of " + decayRate + ")";
    }
}
