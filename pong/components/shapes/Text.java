package game1.components.shapes;

import game1.Shape;
import game1.components.Position;
import game1.components.Renderable;

import java.awt.*;

public class Text implements Shape {
    private String text;
    private Font font = new Font("Arial", Font.PLAIN, 12);

    public Text(String text) {
        this.text = text;
    }

    public Text(String text, Font font) {
        this.text = text;
        this.font = font;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    @Override
    public void draw(Graphics graphics, Position position, Renderable renderable) {
        Color current = graphics.getColor();
        graphics.setColor(renderable.getColor());
        graphics.drawString(text, Math.round(Math.round(position.getX())), Math.round(Math.round(position.getY() + font.getSize())));
        graphics.setColor(current);
    }

    @Override
    public String toString() {
        return text;
    }
}