package pong2.components.renderables;

import pong2.components.renderables.abstracts.RenderableObject;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class Text extends RenderableObject {
    private static final Color DEFAULT_COLOR = Color.BLACK;
    private static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 12);

    private String text;
    private Font font;

    public Text(Point2D.Double point, String text) {
        this(point, text, DEFAULT_COLOR, DEFAULT_FONT);
    }

    public Text(Point2D.Double point, String text, Color color) {
        this(point, text, color, DEFAULT_FONT);
    }

    public Text(Point2D.Double point, String text, Font font) {
        this(point, text, DEFAULT_COLOR, font);
    }

    public Text(Point2D.Double point, String text, Color color, Font font) {
        super(point, color);
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
    public void draw(Graphics graphics) {
        Font initialFont = graphics.getFont();
        Color initialColor = graphics.getColor();
        graphics.setColor(color);
        graphics.setFont(font);
        graphics.drawString(text, Math.round(Math.round(point.getX())),
                Math.round(Math.round(point.getY())) + graphics.getFontMetrics(font).getAscent());
        graphics.setFont(initialFont);
        graphics.setColor(initialColor);
    }

    @Override
    public String toString() {
        return point + " " + color + ": " + text;
    }
}