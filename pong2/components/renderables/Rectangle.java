package pong2.components.renderables;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
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
        Rectangle2D.Double shape = new Rectangle2D.Double(point.getX(), point.getY(), width, height);
        ((Graphics2D) graphics).draw(shape);
        ((Graphics2D) graphics).fill(shape);
        graphics.setColor(initialColor);
    }

    @Override
    public String toString() {
        return color + " rectangle of width " + width + " and height " + height + " at " + point;
    }
}
