package pong;

import pong.components.Position;
import pong.components.Renderable;

import java.awt.Graphics;

public interface Shape {
    void draw(Graphics graphics, Position position, Renderable renderable);
}
