package pong2.subsystems;

import ecs.EntityManager;
import ecs.SubSystem;

import pong2.components.Physics;

public class PhysicsSystem implements SubSystem {
    @Override
    public void processOneGameTick(EntityManager entityManager, double lastFrameTime) {
        for (Physics physics : entityManager.getEntityComponentMapByClass(Physics.class).values()) {
            physics.integrate(lastFrameTime);
        }
    }
}
