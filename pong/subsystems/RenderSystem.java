package game1.subsystems;

import ecs.EntityManager;
import ecs.NonExistentEntityException;
import ecs.SubSystem;
import game1.components.Position;
import game1.components.Renderable;

import javax.swing.JPanel;
import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class RenderSystem extends JPanel implements SubSystem {
    private EntityManager entityManager;

    public RenderSystem(EntityManager entityManager) {
        this.entityManager = entityManager;
        setPreferredSize(new Dimension(250, 250));
    }

    @Override
    public void processOneGameTick(EntityManager entityManager, double lastFrameTime) {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        LinkedList<Map.Entry<UUID, Renderable>> renderableEntities =
                new LinkedList<>(entityManager.getEntityComponentMapByClass(Renderable.class).entrySet());
        Collections.sort(renderableEntities,
                (o1, o2) -> Integer.valueOf(o1.getValue().getZ()).compareTo(o2.getValue().getZ()));
        for (Map.Entry<UUID, Renderable> renderableEntity : renderableEntities) {
            Position position = null;
            try {
                position = entityManager.getComponent(renderableEntity.getKey(), Position.class);
            } catch (NonExistentEntityException e) {
                e.printStackTrace();
            }
            if (position != null) {
                renderableEntity.getValue().draw(graphics, position);
            } else {
                try {
                    System.out.println("Entity " + renderableEntity.getKey() +
                            "(" + entityManager.getEntityName(renderableEntity.getKey()) + ") " +
                            "with renderable component does not have a position component. Weird!");
                } catch (NonExistentEntityException e) {
                    System.out.println("Entity " + renderableEntity.getKey() +
                            "(DEAD) with renderable component does not have a position component. Weird!");
                }

            }
        }
    }
}