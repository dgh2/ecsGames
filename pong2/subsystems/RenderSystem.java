package pong2.subsystems;

import ecs.EntityManager;
import ecs.SubSystem;
import pong2.gui.GameFrame;

import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

public class RenderSystem implements SubSystem {
    private GameFrame gameFrame;

    public RenderSystem(final EntityManager entityManager) {
        try {
            EventQueue.invokeAndWait(() -> gameFrame = new GameFrame(entityManager));
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        EventQueue.invokeLater(() -> gameFrame.setVisible(true));
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