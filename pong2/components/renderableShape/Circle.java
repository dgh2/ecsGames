package pong2.components.renderableShape;

import java.awt.Color;
import java.awt.Graphics;
import pong2.components.Renderable;

public class Circle implements Renderable {
    private int x, y, diameter;
    private Color color;

    public Circle(int x, int y, int diameter, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.diameter = diameter;
    }

    @Override
    public void draw(Graphics graphics) {
        Color backupColor = graphics.getColor();
        graphics.setColor(color);
        graphics.drawOval(x, y, diameter, diameter);
        graphics.setColor(backupColor);
    }

    public int getdiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }
}
