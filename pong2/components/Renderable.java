package pong2.components;

import ecs.Component;
import java.awt.Graphics;

public interface Renderable extends Component {
    void draw(Graphics graphics);
}
