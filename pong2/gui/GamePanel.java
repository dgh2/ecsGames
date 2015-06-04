package pong2.gui;

import ecs.EntityManager;
import ecs.NonExistentEntityException;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import pong2.components.Position;
import pong2.components.Renderable;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import pong2.components.renderableShape.Circle;
import pong2.components.renderableShape.Rectangle;
import pong2.components.renderableShape.Text;

public class GamePanel extends JPanel {
    private EntityManager entityManager;

    public GamePanel(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        //TODO: solve this problem by integrating systems with the entityManager
        //TODO: so adding any Renderable component to an entity registers that entity & component with the system
        for (Renderable renderable : entityManager.getEntityComponentMapByClass(Rectangle.class).values()) {
            renderable.draw(graphics);
        }
        for (Renderable renderable : entityManager.getEntityComponentMapByClass(Circle.class).values()) {
            renderable.draw(graphics);
        }
        for (Renderable renderable : entityManager.getEntityComponentMapByClass(Text.class).values()) {
            renderable.draw(graphics);
        }
    }

//    private void delegatePainting(Graphics graphics) {
//        new SwingWorker() {
//            @Override
//            protected String doInBackground() throws InterruptedException {
//                //Get all Renderables in a background thread and draw each in the Event Dispatch thread
//                for (Renderable renderable : entityManager.getEntityComponentMapByClass(Renderable.class).values()) {
//                    EventQueue.invokeLater(() -> renderable.draw(graphics));
//                }
//                return null;
//            }
//        }.execute();
//    }
}
