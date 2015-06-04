package pong2.components;

import ecs.Component;

public class Velocity implements Component {
    protected double magnitude, direction;
    public Velocity(double magnitude, double direction) {
        this.magnitude = magnitude;
        this.direction = direction;
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

    @Override
    public String toString() {
        return "(" + magnitude + " @ " + direction + " degrees)";
    }
}
