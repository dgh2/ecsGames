package pong.subsystems;

import ecs.EntityManager;
import ecs.NonExistentEntityException;
import ecs.SubSystem;
import pong.components.Input;
import pong.components.Position;
import pong.components.Velocity;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CollisionSystem implements SubSystem {
    @Override
    public void processOneGameTick(EntityManager entityManager, double lastFrameTime) {
        HashMap<UUID, Velocity> entityVelocityMap = entityManager.getEntityComponentMapByClass(Velocity.class);
        for (Map.Entry<UUID, Velocity> velocityEntity : entityVelocityMap.entrySet()) {
            Velocity velocity = velocityEntity.getValue();
            Position position = null;
            Input input = null;
            try {
                position = entityManager.getComponent(velocityEntity.getKey(), Position.class);
                input = entityManager.getComponent(velocityEntity.getKey(), Input.class);
            } catch (NonExistentEntityException e) {
                e.printStackTrace();
            }

            //update position
            if (position != null) {
                position.setX(position.getX() + (velocity.getMagnitude() * Math.cos(velocity.getDirection())));
                position.setY(position.getY() + (velocity.getMagnitude() * Math.sin(velocity.getDirection())));
            } else {
                try {
                    System.out.println("Entity " + velocityEntity.getKey() +
                            "(" + entityManager.getEntityName(velocityEntity.getKey()) + ") " +
                            "with velocity component does not have a position component. Weird!");
                } catch (NonExistentEntityException e) {
                    System.out.println("Entity " + velocityEntity.getKey() +
                            "(DEAD) with velocity component does not have a position component. Weird!");
                }
            }

            //update velocity
            velocity.setMagnitude(velocity.getMagnitude() * (1 - velocity.getDecayRate()));
            if (input != null) {
                if (input.getDirection() == velocity.getDirection()) {
                    velocity.setMagnitude(velocity.getMagnitude() + input.getForce());
                } else if (input.getDirection() == (360 - velocity.getDirection()) % 360) {
                    velocity.setMagnitude(velocity.getMagnitude() - input.getForce());
                    if (velocity.getMagnitude() < 0) {
                        velocity.setMagnitude(-1 * velocity.getMagnitude());
                        velocity.setDirection((360 - velocity.getDirection()) % 360);
                    }
                }
            }
        }
    }
}
