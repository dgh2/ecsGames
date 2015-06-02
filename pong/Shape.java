package game1;

import game1.components.Position;
import game1.components.Renderable;

import java.awt.Graphics;

public interface Shape {
    void draw(Graphics graphics, Position position, Renderable renderable);
}
