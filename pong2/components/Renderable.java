package pong2.components;

import ecs.Component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;

public class Renderable implements Component {
    private Shape shape;
    private Color color;

    public Renderable(Shape shape, Color color) {
        this.shape = shape;
        this.color = color;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void draw(Graphics graphics, Position position) {
//        shape.draw(graphics, position, this);
    }

    @Override
    public String toString() {
        return color + " " + shape.;
    }
}
