package pong2.components.renderables;

import pong2.components.renderables.abstracts.RenderableObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class Circle extends RenderableObject {
    private double diameter;

    public Circle(Point2D.Double point, double diameter, Color color) {
        super(point, color);
        this.diameter = diameter;
    }

    public double getDiameter() {
        return diameter;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    @Override
    public void draw(Graphics graphics) {
        Color initialColor = graphics.getColor();
        graphics.setColor(color);
        graphics.fillOval(Math.round(Math.round(point.getX())), Math.round(Math.round(point.getY())),
                Math.round(Math.round(diameter)), Math.round(Math.round(diameter)));
        graphics.setColor(initialColor);
    }

    @Override
    public String toString() {
        return color + " circle of diameter " + diameter + " at " + point;
    }
}
