package pong2.subsystems;

import ecs.EntityManager;
import ecs.SubSystem;
import pong2.components.Renderable;

public class CollisionSystem implements SubSystem {
    @Override
    public void processOneGameTick(EntityManager entityManager, double lastFrameTime) {
//        for (Renderable renderable : entityManager.getEntityComponentMapByClass(Renderable.class).values()) {
//            if (renderable.getObject()) {
//
//            }
//        }
    }
}
