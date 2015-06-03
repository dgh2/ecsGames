package pong2.subsystems;

import ecs.EntityManager;
import ecs.SubSystem;
import pong2.gui.GameFrame;

import java.awt.EventQueue;
import java.awt.event.WindowEvent;

public class RenderSystem implements SubSystem {
    private GameFrame gameFrame = null;
    private EntityManager entityManager;

    public RenderSystem(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void start() {
        EventQueue.invokeLater(() -> gameFrame = new GameFrame(entityManager));
    }

    public void stop() {
        EventQueue.invokeLater(() -> gameFrame.dispatchEvent(new WindowEvent(gameFrame, WindowEvent.WINDOW_CLOSING)));
    }

    public void forceStop() {
        EventQueue.invokeLater(gameFrame::dispose);
    }

    @Override
    public void processOneGameTick(EntityManager entityManager, double lastFrameTime) {
        EventQueue.invokeLater(gameFrame::repaint);
    }
}