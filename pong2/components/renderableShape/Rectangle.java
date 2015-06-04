package pong2.components.renderableShape;

import java.awt.Color;
import java.awt.Graphics;
import pong2.components.Renderable;

public class Rectangle implements Renderable {
    private int x, y, width, height;
    private Color color;

    public Rectangle(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics graphics) {
        Color backupColor = graphics.getColor();
        graphics.setColor(color);
        graphics.drawRect(x, y, width, height);
        graphics.setColor(backupColor);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
