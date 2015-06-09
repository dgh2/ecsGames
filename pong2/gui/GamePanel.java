package pong2.gui;

import ecs.EntityManager;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import pong2.components.Renderable;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class GamePanel extends JPanel {
    private EntityManager entityManager;
    private boolean antiAliasing = true;

    public GamePanel(EntityManager entityManager) {
        this.entityManager = entityManager;
        setFocusable(true);
        requestFocusInWindow();
    }

    public boolean getAntiAliasing() {
        return antiAliasing;
    }

    public void setAntiAliasing(boolean antiAliasing) {
        this.antiAliasing = antiAliasing;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = Graphics2D.class.cast(graphics);
        super.paintComponent(graphics);
        if (antiAliasing) {
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                    RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        }

        LinkedList<Renderable> renderables = new LinkedList<>(entityManager.getEntityComponentMapByClass(Renderable.class).values());
        Collections.sort(renderables);
        for (Renderable renderable : renderables) {
            renderable.draw(graphics2D);
        }
    }
}
