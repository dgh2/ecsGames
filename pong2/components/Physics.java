package pong2.components;

import ecs.Component;
import java.awt.geom.Point2D;

public class Physics implements Component {
    private double mass = 1;
    private Point2D.Double location;
    private Point2D.Double acceleration = new Point2D.Double();
    private Point2D.Double velocity = new Point2D.Double();
    private Point2D.Double force = new Point2D.Double();

    public Physics(Point2D.Double location) {
        this.location = location;
    }

    public void integrate(double timestep) {
        Point2D.Double old_acceleration = acceleration;
        location.setLocation(vectorAddition(
                vectorAddition(location, vectorScaling(velocity, timestep)),
                vectorScaling(old_acceleration, Math.pow(timestep, 2) / 2)));
        acceleration = vectorScaling(force, 1 / mass);
        force = new Point2D.Double();
        velocity = vectorAddition(velocity, vectorScaling(vectorAddition(old_acceleration, old_acceleration), timestep / 2));
    }

    public void addForce(Point2D.Double force) {
        this.force = vectorAddition(this.force, force);
    }

    private static Point2D.Double vectorAddition(Point2D.Double vector1, Point2D.Double vector2) {
        return new Point2D.Double(vector1.getX() + vector2.getX(), vector1.getY() + vector2.getY());
    }

    private static Point2D.Double vectorScaling(Point2D.Double vector, double scalar) {
        return new Point2D.Double(vector.getX() * scalar, vector.getY() * scalar);
    }
}
