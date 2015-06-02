package pong.components;

import ecs.Component;
import pong.Shape;

import java.awt.Color;
import java.awt.Graphics;

public class Renderable implements Component {
    private int z;
    private Shape shape;
    private Color color;

    public <T extends Shape> Renderable(int z, T shape, Color color) {
        this.z = z;
        this.shape = shape;
        this.color = color;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
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
        shape.draw(graphics, position, this);
    }

    @Override
    public String toString() {
        return color + " " + shape;
    }
}
