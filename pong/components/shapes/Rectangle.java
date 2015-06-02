package game1.components.shapes;

import game1.Shape;
import game1.components.Position;
import game1.components.Renderable;

import java.awt.Color;
import java.awt.Graphics;

public class Rectangle implements Shape {
    private double width, height;

    public Rectangle(double width, double height) {
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
    public void draw(Graphics graphics, Position position, Renderable renderable) {
        Color current = graphics.getColor();
        graphics.setColor(renderable.getColor());
        graphics.fillRect(Math.round(Math.round(position.getX())), Math.round(Math.round(position.getY())),
                Math.round(Math.round(width)), Math.round(Math.round(height)));
        graphics.setColor(current);
    }

    @Override
    public String toString() {
        return "Rectangle of width " + width + " and height " + height;
    }
}
