package pong2;

import ecs.EntityManager;
import pong2.subsystems.InputSystem;
import pong2.subsystems.RenderSystem;

import javax.swing.JFrame;

public class Main extends JFrame {

    private final EntityManager entityManager = new EntityManager();
    private final RenderSystem renderSystem = new RenderSystem(entityManager);
//    private final PhysicsSystem physicsSystem = new PhysicsSystem();
    private final InputSystem inputSystem = new InputSystem();

    private static final int TARGET_FPS = 60;
    private static final double OPTIMAL_TIME = 1000000 / TARGET_FPS;

//    private UUID background;
//    private UUID paddle1;
//    private UUID paddle2;
//    private UUID ball;

    private boolean gameRunning = true;

//    public static int WALL_BUFFER_SPACE = 10;
    public static double FPS_WEIGHT_RATIO = 0.9;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        super();
        renderSystem.start();
        inputSystem.start(renderSystem);
        initializeGame();
        startGameloop();
    }

    /*
    private void initializeInput() {
        inputSystem.addControl('q', () -> {
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
        inputSystem.duplicateControl('j', 'k', 'l', ';');
    }

    private void initializeGame() {
        background = entityManager.createEntity("background");
        paddle1 = entityManager.createEntity("paddle1");
        paddle2 = entityManager.createEntity("paddle2");
        ball = entityManager.createEntity("ball");
        Random random = new Random();
        try {
            entityManager.addComponent(background, new Position(0, 0));
            entityManager.addComponent(background, new Renderable(0, new Rectangle(jContentPane.getWidth(), jContentPane.getHeight()), Color.BLACK));
            entityManager.addComponent(paddle1, new Velocity(0, 0, 0.66));
            Renderable paddle1Renderable = new Renderable(1, new Rectangle(10, jContentPane.getHeight() * .1), Color.WHITE);
            entityManager.addComponent(paddle1, paddle1Renderable);
            entityManager.addComponent(paddle1,
                    new Position(WALL_BUFFER_SPACE,
                            Math.round(jContentPane.getHeight() - ((Rectangle) paddle1Renderable.getShape()).getHeight()) / 2));
            entityManager.addComponent(paddle1, new Input(.5));
            entityManager.addComponent(paddle2, new Velocity(0, 0, 0.66));
            Renderable paddle2Renderable = new Renderable(1, new Rectangle(10, jContentPane.getHeight() * .1), Color.WHITE);
            entityManager.addComponent(paddle2, paddle2Renderable);
            entityManager.addComponent(paddle2,
                    new Position(Math.round(jContentPane.getWidth() - ((Rectangle) paddle2Renderable.getShape()).getWidth() - WALL_BUFFER_SPACE),
                            Math.round(jContentPane.getHeight() - ((Rectangle) paddle2Renderable.getShape()).getHeight()) / 2));
            Renderable ballRenderable = new Renderable(2, new Circle(8), Color.WHITE);
            entityManager.addComponent(paddle2, new Input(.5));
            entityManager.addComponent(ball, ballRenderable);
            Position ballPosition = new Position(Math.round(Math.round((jContentPane.getWidth() - ((Circle) ballRenderable.getShape()).getDiameter()) / 2)),
                    Math.round(Math.round(((jContentPane.getHeight() - ((Circle) ballRenderable.getShape()).getDiameter()) / 2))));
            entityManager.addComponent(ball, ballPosition);
            entityManager.addComponent(ball, new Velocity(random.nextInt(5) + 3,
                    (random.nextBoolean() ? 0 : 180), 0.001));
        } catch (NonExistentEntityException e) {
            e.printStackTrace();
        }
        startGameloop();
    }*/

    private void initializeGame() {
//        try {
//        } catch (NonExistentEntityException e) {
//            e.printStackTrace();
//        }
    }

    public void processOneGameTick(double lastFrameTime) {
//        physicsSystem.processOneGameTick(entityManager, lastFrameTime);
        renderSystem.processOneGameTick(entityManager, lastFrameTime);
//        inputSystem.processOneGameTick(entityManager, lastFrameTime);
    }

    private void startGameloop() {
        double lastLoopTime = System.nanoTime();
        double previousUpdateLength = System.nanoTime();
        double avgFps;

        final int TARGET_FPS = 60;
        final double OPTIMAL_TIME = 1000000000 / TARGET_FPS;

        while (gameRunning) {
            // work out how long its been since the last update
            // this will be used to calculate how far the entities should move this loop
            double now = System.nanoTime();
            double updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / (OPTIMAL_TIME);

            // update the frame counter
            avgFps = Math.floor(1000000000 / ((updateLength * (1.0 - FPS_WEIGHT_RATIO)) + (previousUpdateLength * FPS_WEIGHT_RATIO)));
            previousUpdateLength = updateLength;

            renderSystem.setFPS(avgFps);

            // update the game logic
            processOneGameTick(delta);

            // We want each frame to take 10 milliseconds, to do this, we've recorded when we started the frame.
            // We add 10 milliseconds to this and factor in the current time to give us our final value to wait for.
            // Remember, this is in ms, whereas our lastLoopTime etc. vars are in ns.
            try {
                long sleep = Math.round(lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
                if (sleep > 0) {
                    Thread.sleep(sleep);
                }
            } catch (InterruptedException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        renderSystem.stop();
    }
}