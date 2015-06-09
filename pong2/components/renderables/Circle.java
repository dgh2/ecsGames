package pong2.components.renderables;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
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
        Ellipse2D.Double shape = new Ellipse2D.Double(point.getX(), point.getY(), diameter, diameter);
        ((Graphics2D) graphics).draw(shape);
        ((Graphics2D) graphics).fill(shape);
        graphics.setColor(initialColor);
    }

    @Override
    public String toString() {
        return color + " circle of diameter " + diameter + " at " + point;
    }
}
