package pong2.components;
public class Forces {
    private double magnitude, direction;

    public double getXComponent() {
        return magnitude * Math.cos(Math.toRadians(direction));
    }

    public double getYComponent() {
        return magnitude * Math.sin(Math.toRadians(direction));
    }

    public double apply(double magnitude, double direction) {
        return magnitude * Math.sin(Math.toRadians(direction));
    }
}
