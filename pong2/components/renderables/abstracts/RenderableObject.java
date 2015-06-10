package pong2.components.renderables.abstracts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract class RenderableObject {
    protected Point2D.Double point;
    protected Color color;

    public RenderableObject(Point2D.Double point, Color color) {
        this.point = point;
        this.color = color;
    }

    public Point2D.Double getPoint() {
        return point;
    }

    public void setPoint(Point2D.Double point) {
        this.point = point;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public abstract void draw(Graphics graphics);

    public abstract String toString();
}
