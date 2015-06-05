package pong2.subsystems;

import ecs.EntityManager;
import ecs.NonExistentEntityException;
import ecs.SubSystem;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import pong2.components.Renderable;
import pong2.components.renderables.Text;
import pong2.gui.GameFrame;

import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import pong2.gui.GamePanel;

public class RenderSystem implements SubSystem {
    private GameFrame gameFrame = null;
    private EntityManager entityManager;
    private Text fpsLabel = new Text(new Point2D.Double(2, 2), "FPS: ?", Color.BLACK, new Font("Arial", Font.PLAIN, 10));
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
                entityManager.addComponent(entityManager.createEntity("fpsLabel"), new Renderable(fpsLabel));
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

    public void setFPS(double avgFps) {
        fpsLabel.setText("FPS: " + Math.round(Math.round(Math.floor(avgFps))));
    }

    @Override
    public void processOneGameTick(EntityManager entityManager, double lastFrameTime) {
        if (gameFrame != null) {
            EventQueue.invokeLater(gameFrame::repaint);
        }
    }
}