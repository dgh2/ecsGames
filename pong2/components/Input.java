package pong2.components;

import ecs.Component;

public class Input implements Component {
    private double direction;

    public Input(double direction) {
        this.direction = direction;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }
}
