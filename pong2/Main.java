package pong2;

import ecs.EntityManager;
import pong2.subsystems.InputSystem;
import pong2.subsystems.PhysicsSystem;
import pong2.subsystems.RenderSystem;

public class Main {
    private final EntityManager entityManager = new EntityManager();
    private final RenderSystem renderSystem = new RenderSystem(entityManager);
    private final PhysicsSystem physicsSystem = new PhysicsSystem();
    private final InputSystem inputSystem = new InputSystem();

    private static final int TARGET_FPS = 60;
    private static final double OPTIMAL_TIME = 1000000 / TARGET_FPS;

//    public static int WALL_BUFFER_SPACE = 10;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        super();
        initializeGame();
        renderSystem.start();
        inputSystem.start(renderSystem);
        initializeInput();
        startGameloop();
    }

    private void initializeInput() {
        /*inputSystem.addControl('q', () -> {
            try {
                entityManager.getComponent(paddle1, Input.class).setDirection(270);
            } catch (NonExistentEntityException e) {
                e.printStackTrace();
            }
        });
        inputSystem.duplicateControl('q', 'w', 'e', 'r');
        inputSystem.addControl('a', () -> {
            try {
                entityManager.getComponent(paddle1, Input.class).setDirection(90);
            } catch (NonExistentEntityException e) {
                e.printStackTrace();
            }
        });
        inputSystem.duplicateControl('a', 's', 'd', 'f');
        inputSystem.addControl('u', () -> {
            try {
                entityManager.getComponent(paddle2, Input.class).setDirection(270);
            } catch (NonExistentEntityException e) {
                e.printStackTrace();
            }
        });
        inputSystem.duplicateControl('u', 'i', 'o', 'p');
        inputSystem.addControl('j', () -> {
            try {
                entityManager.getComponent(paddle2, Input.class).setDirection(90);
            } catch (NonExistentEntityException e) {
                e.printStackTrace();
            }
        });
        inputSystem.duplicateControl('j', 'k', 'l', ';');*/
    }

    private void initializeGame() {
//        background = entityManager.createEntity("background");
//        paddle1 = entityManager.createEntity("paddle1");
//        paddle2 = entityManager.createEntity("paddle2");
//        ball = entityManager.createEntity("ball");
//        Random random = new Random();


//        entityManager.addComponent(fpsLabel, new Position(50, 3));
//        entityManager.addComponent(fpsLabel, fpsLabelRenderable);
//        entityManager.addComponent(background, new Position(0, 0));
//        entityManager.addComponent(background, new Renderable(0, new Rectangle(jContentPane.getWidth(), jContentPane.getHeight()), Color.BLACK));
//        entityManager.addComponent(paddle1, new Velocity(0, 0, 0.66));
//        Renderable paddle1Renderable = new Renderable(1, new Rectangle(10, jContentPane.getHeight() * .1), Color.WHITE);
//        entityManager.addComponent(paddle1, paddle1Renderable);
//        entityManager.addComponent(paddle1,
//                new Position(WALL_BUFFER_SPACE,
//                        Math.round(jContentPane.getHeight() - ((Rectangle) paddle1Renderable.getShape()).getHeight()) / 2));
//        entityManager.addComponent(paddle1, new Input(.5));
//        entityManager.addComponent(paddle2, new Velocity(0, 0, 0.66));
//        Renderable paddle2Renderable = new Renderable(1, new Rectangle(10, jContentPane.getHeight() * .1), Color.WHITE);
//        entityManager.addComponent(paddle2, paddle2Renderable);
//        entityManager.addComponent(paddle2,
//                new Position(Math.round(jContentPane.getWidth() - ((Rectangle) paddle2Renderable.getShape()).getWidth() - WALL_BUFFER_SPACE),
//                        Math.round(jContentPane.getHeight() - ((Rectangle) paddle2Renderable.getShape()).getHeight()) / 2));
//        Renderable ballRenderable = new Renderable(2, new Circle(8), Color.WHITE);
//        entityManager.addComponent(paddle2, new Input(.5));
//        entityManager.addComponent(ball, ballRenderable);
//        Position ballPosition = new Position(Math.round(Math.round((jContentPane.getWidth() - ((Circle) ballRenderable.getShape()).getDiameter()) / 2)),
//                Math.round(Math.round(((jContentPane.getHeight() - ((Circle) ballRenderable.getShape()).getDiameter()) / 2))));
//        entityManager.addComponent(ball, ballPosition);
//        entityManager.addComponent(ball, new Velocity(random.nextInt(5) + 3,
//                (random.nextBoolean() ? 0 : 180), 0.001));
    }

    public void processOneGameTick(double lastFrameTime) {
        physicsSystem.processOneGameTick(entityManager, lastFrameTime);
        inputSystem.processOneGameTick(entityManager, lastFrameTime);
    }

    private void startGameloop() {
        boolean runFlag = true;

        double delta = 1;

        renderSystem.start();
        // convert the time to seconds
        double nextTime = (double)System.nanoTime() / 1000000000.0;
        double maxTimeDiff = 0.5;
        int skippedFrames = 1;
        int maxSkippedFrames = 5;
        while (runFlag) {
            // convert the time to seconds
            double currTime = (double)System.nanoTime() / 1000000000.0;
            if ((currTime - nextTime) > maxTimeDiff) {
                nextTime = currTime;
            }
            if (currTime >= nextTime) {
                // assign the time for the next update
                nextTime += delta;
                processOneGameTick(delta);
                if((currTime < nextTime) || (skippedFrames > maxSkippedFrames)) {
                    renderSystem.processOneGameTick(entityManager, delta);
                    skippedFrames = 1;
                } else {
                    skippedFrames++;
                }
            } else {
                // calculate the time to sleep
                int sleepTime = (int)(1000.0 * (nextTime - currTime));
                // sanity check
                if (sleepTime > 0) {
                    // sleep until the next update
                    try {
                        Thread.sleep(sleepTime);
                    } catch(InterruptedException e) {
                        // do nothing
                    }
                }
            }
        }
        renderSystem.stop();


//        double gameLoopStart = System.nanoTime();
//
//        double currentLoopStart;
//        double lastLoopStart = gameLoopStart;
//        while (lastLoopStart - gameLoopStart < 100000000) {//(1000000000d * 5)) {
//            // calculate how far the entities should move this loop based on how long its been since the last update
//            currentLoopStart = System.nanoTime();
//
//            // update the game logic
//            processOneGameTick(currentLoopStart - lastLoopStart);
//
//            // We want each frame to take 10 milliseconds, to do this, we've recorded when we started the frame.
//            // We add 10 milliseconds to this and factor in the current time to give us our final value to wait for.
//            // Remember, this is in ms, whereas our lastLoopTime etc. vars are in ns.
//            try {
//                double workTime = System.nanoTime() - currentLoopStart;
//                double sleepTime = OPTIMAL_TIME - workTime;
//                System.out.println("Worked for " + workTime + "ns, Sleeping for " + sleepTime + "ns");
//                if (sleepTime > 0) {
//                    Thread.sleep(0, Math.round(Math.round(Math.floor(sleepTime))));
//                } else {
//                    System.out.println("Running behind! No time to sleep!");
//                }
//            } catch (InterruptedException | IllegalArgumentException e) {
//                e.printStackTrace();
//            }
//            lastLoopStart = currentLoopStart;
//        }
//        renderSystem.stop();
    }
}