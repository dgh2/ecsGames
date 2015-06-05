package pong2.gui;

import ecs.EntityManager;
import pong2.components.Renderable;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Map;
import java.util.UUID;

public class GamePanel extends JPanel {
    private EntityManager entityManager;
    private boolean antiAliasing = true;

    public GamePanel(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public boolean getAntiAliasing() {
        return antiAliasing;
    }

    public void setAntiAliasing(boolean antiAliasing) {
        this.antiAliasing = antiAliasing;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (antiAliasing) {
            Graphics2D.class.cast(graphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Graphics2D.class.cast(graphics).setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                    RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        }

        for (Map.Entry<UUID, Renderable> renderableEntity : entityManager.getEntityComponentMapByClass(Renderable.class).entrySet()) {
            renderableEntity.getValue().draw(graphics);
        }
    }
}
