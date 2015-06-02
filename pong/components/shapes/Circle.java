package pong.components.shapes;

import pong.Shape;
import pong.components.Position;
import pong.components.Renderable;

import java.awt.Color;
import java.awt.Graphics;

public class Circle implements Shape {
    private double diameter;

    public Circle(double diameter) {
        this.diameter = diameter;
    }

    public double getDiameter() {
        return diameter;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    @Override
    public void draw(Graphics graphics, Position position, Renderable renderable) {
        Color current = graphics.getColor();
        graphics.setColor(renderable.getColor());
        graphics.fillOval(Math.round(Math.round(position.getX())), Math.round(Math.round(position.getY())),
                Math.round(Math.round(diameter)), Math.round(Math.round(diameter)));
        graphics.setColor(current);
    }

    @Override
    public String toString() {
        return "Circle of diameter " + diameter;
    }
}
