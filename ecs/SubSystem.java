package ecs;

public interface SubSystem {
    void processOneGameTick(EntityManager entityManager, double lastFrameTime);
}
