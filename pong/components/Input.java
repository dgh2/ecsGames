package pong.components;

import ecs.Component;

public class Input implements Component {
    private double force, direction, decayRate;

    public Input(double decayRate) {
        this(0, 0, decayRate);
    }

    public Input(double force, double direction, double decayRate) {
        this.force = force;
        this.direction = direction;
        this.decayRate = decayRate;
    }

    public double getForce() {
        return force;
    }

    public void setForce(double force) {
        this.force = force;
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
}
