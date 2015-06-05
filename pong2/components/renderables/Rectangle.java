package pong2.components.renderables;

import pong2.components.renderables.abstracts.RenderableObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class Rectangle extends RenderableObject {
    private double width, height;

    public Rectangle(Point2D.Double point, double width, double height, Color color) {
        super(point, color);
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public void draw(Graphics graphics) {
        Color initialColor = graphics.getColor();
        graphics.setColor(color);
        graphics.fillRect(Math.round(Math.round(point.getX())), Math.round(Math.round(point.getY())),
                Math.round(Math.round(width)), Math.round(Math.round(height)));
        graphics.setColor(initialColor);
    }

    @Override
    public String toString() {
        return color + " rectangle of width " + width + " and height " + height + " at " + point;
    }
}
