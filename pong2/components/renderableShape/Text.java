package pong2.components.renderableShape;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import pong2.components.Renderable;

public class Text implements Renderable {
    private int x, y;
    private Color color;
    private Font font;
    private String text;

    public Text(int x, int y, String text, Color color) {
        this(x, y, text, color, new Font("Arial", Font.PLAIN, 10));
    }

    public Text(int x, int y, String text, Color color, Font font) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.text = text;
        this.font = font;
    }

    @Override
    public void draw(Graphics graphics) {
        Color backupColor = graphics.getColor();
        Font backupFont = graphics.getFont();
        graphics.setColor(color);
        graphics.setFont(font);
        graphics.drawString(text, x, y);
        graphics.setColor(backupColor);
        graphics.setFont(backupFont);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
    public String toString() {
        return "[" + color + " (" + x + "," + y + ")] " + text;
    }
}
