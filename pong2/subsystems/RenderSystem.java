package pong2.subsystems;

import ecs.EntityManager;
import ecs.NonExistentEntityException;
import ecs.SubSystem;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import pong2.components.renderableShape.Text;
import pong2.gui.GameFrame;

import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import pong2.gui.GamePanel;
import java.lang.reflect.InvocationTargetException;

public class RenderSystem implements SubSystem {
    private GameFrame gameFrame = null;
    private EntityManager entityManager;
    private Text fpsLabel = new Text(2, 10, "FPS: ?", Color.BLACK, new Font("Arial", Font.PLAIN, 10));
//    public static double FPS_WEIGHT_RATIO = 0.7;

    public RenderSystem(EntityManager entityManager) {
        this.entityManager = entityManager;
        try {
            EventQueue.invokeAndWait(() -> gameFrame = new GameFrame(entityManager));
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        EventQueue.invokeLater(() -> {
            try {
                entityManager.addComponent(entityManager.createEntity("fpsLabel"), fpsLabel);
            } catch (NonExistentEntityException e) {
                e.printStackTrace();
            }
            gameFrame.setVisible(true);
        });
    }

    public void stop() {
        EventQueue.invokeLater(() -> gameFrame.dispatchEvent(new WindowEvent(gameFrame, WindowEvent.WINDOW_CLOSING)));
    }

    public void dispose() {
        EventQueue.invokeLater(gameFrame::dispose);
    }

    public GamePanel getGamePanel() {
        if (gameFrame != null && gameFrame.getContentPane() != null && gameFrame.getContentPane() instanceof GamePanel) {
            return (GamePanel) gameFrame.getContentPane();
        }
        return null;
    }

    @Override
    public void processOneGameTick(EntityManager entityManager, double lastFrameTime) {
        if (gameFrame != null) {
            EventQueue.invokeLater(gameFrame::repaint);
        }
    }
}