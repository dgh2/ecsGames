package pong2.components;

import ecs.Component;

public class Momentum extends Velocity {
    private double mass;

    public Momentum(double mass, double magnitude, double direction) {
        super(magnitude, direction);
        this.mass = mass;
    }

    public double getMass() {
        return mass;
    }
}
