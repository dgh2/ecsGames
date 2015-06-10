package pong2;

import ecs.EntityManager;
import ecs.NonExistentEntityException;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.UUID;
import pong2.components.Physics;
import pong2.components.Renderable;
import pong2.components.renderables.Circle;
import pong2.components.renderables.Rectangle;
import pong2.subsystems.CollisionSystem;
import pong2.subsystems.InputSystem;
import pong2.subsystems.PhysicsSystem;
import pong2.subsystems.RenderSystem;

import javax.swing.JFrame;

public class Main extends JFrame {
    public static int WALL_BUFFER_SPACE = 25;

    private final EntityManager entityManager = new EntityManager();
    private final RenderSystem renderSystem = new RenderSystem(entityManager);
    private final PhysicsSystem physicsSystem = new PhysicsSystem();
    private final CollisionSystem collisionSystem = new CollisionSystem();
    private final InputSystem inputSystem = new InputSystem(renderSystem);

    private static final int TARGET_FPS = 60;
    private static final double OPTIMAL_TIME = 1000000000 / TARGET_FPS;

    private UUID paddle1;
    private UUID paddle2;
    private UUID ball;

    private boolean gameRunning = true;

    public static double FPS_WEIGHT_RATIO = 0.7;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        super();
        initializeInput();
        initializeGame();
        startGameloop();
    }

    private void initializeInput() {
        inputSystem.addControl('q', () -> {
            try {
                entityManager.getComponent(paddle1, Physics.class).addForce(new Point2D.Double(0, -1));
            } catch (NonExistentEntityException e) {
                e.printStackTrace();
            }
        });
        inputSystem.duplicateControl('q', 'w', 'e', 'r');
        inputSystem.addControl('a', () -> {
            try {
                entityManager.getComponent(paddle1, Physics.class).addForce(new Point2D.Double(0, 1));
            } catch (NonExistentEntityException e) {
                e.printStackTrace();
            }
        });
        inputSystem.duplicateControl('a', 's', 'd', 'f');
        inputSystem.addControl('u', () -> {
            try {
                entityManager.getComponent(paddle2, Physics.class).addForce(new Point2D.Double(0, -1));
            } catch (NonExistentEntityException e) {
                e.printStackTrace();
            }
        });
        inputSystem.duplicateControl('u', 'i', 'o', 'p');
        inputSystem.addControl('j', () -> {
            try {
                entityManager.getComponent(paddle2, Physics.class).addForce(new Point2D.Double(0, 1));
            } catch (NonExistentEntityException e) {
                e.printStackTrace();
            }
        });
        inputSystem.duplicateControl('j', 'k', 'l', ';');
    }

    private void initializeGame() {
        paddle1 = entityManager.createEntity("paddle1");
        paddle2 = entityManager.createEntity("paddle2");
        ball = entityManager.createEntity("ball");
        Random random = new Random();
        try {
            double paddleWidth = renderSystem.getGamePanel().getWidth() * 0.011;
            double paddleHeight = renderSystem.getGamePanel().getHeight() * 0.1;
            double paddleY = (renderSystem.getGamePanel().getHeight() - paddleHeight) / 2;
            double ballDiameter = 14;
            double ballSpeed = 10;

            Point2D.Double paddle1Location = new Point2D.Double(WALL_BUFFER_SPACE, paddleY);
            Point2D.Double paddle2Location = new Point2D.Double(
                    renderSystem.getGamePanel().getWidth() - paddleWidth - WALL_BUFFER_SPACE, paddleY);
            Point2D.Double ballLocation = new Point2D.Double((renderSystem.getGamePanel().getWidth() - ballDiameter) / 2,
                    (renderSystem.getGamePanel().getHeight() - ballDiameter) / 2);

            entityManager.addComponent(paddle1, new Renderable(new Rectangle(
                    paddle1Location, paddleWidth, paddleHeight, Color.WHITE)));
            entityManager.addComponent(paddle2, new Renderable(new Rectangle(
                    paddle2Location, paddleWidth, paddleHeight, Color.WHITE)));
            entityManager.addComponent(ball, new Renderable(new Circle(
                    ballLocation, ballDiameter, Color.WHITE)));

            entityManager.addComponent(paddle1, new Physics(paddle1Location));
            entityManager.addComponent(paddle2, new Physics(paddle2Location));

            Physics ballPhysics = new Physics(ballLocation);
            ballPhysics.addForce(new Point2D.Double((random.nextBoolean() ? 1 : -1) * ballSpeed, 0));
            entityManager.addComponent(ball, ballPhysics);
        } catch (NonExistentEntityException e) {
            e.printStackTrace();
        }
    }

    public void processOneGameTick(double lastFrameTime) {
        inputSystem.processOneGameTick(entityManager, lastFrameTime);
        physicsSystem.processOneGameTick(entityManager, lastFrameTime);
        collisionSystem.processOneGameTick(entityManager, lastFrameTime);
        renderSystem.processOneGameTick(entityManager, lastFrameTime);
    }

    private void startGameloop() {
        double lastLoopTime = System.nanoTime();
        double previousUpdateLength = System.nanoTime();
        double avgFps;

        while (gameRunning) {
            // work out how long its been since the last update
            // this will be used to calculate how far the entities should move this loop
            double now = System.nanoTime();
            double updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / (OPTIMAL_TIME);

            // update the frame counter
            avgFps = Math.floor(1000000000 / ((updateLength * (1 - FPS_WEIGHT_RATIO)) + (previousUpdateLength * FPS_WEIGHT_RATIO)));
//            avgFps = Math.floor(1000000000 / updateLength);
            previousUpdateLength = updateLength;

            renderSystem.setFPS(avgFps);

            // update the game logic
            processOneGameTick(delta);

            // We want each frame to take 10 milliseconds, to do this, we've recorded when we started the frame.
            // We add 10 milliseconds to this and factor in the current time to give us our final value to wait for.
            // Remember, this is in ms, whereas our lastLoopTime etc. vars are in ns.
            try {
//                System.out.println("Worked for " + ((System.nanoTime() - lastLoopTime) / 1000000) + "ms");
//                System.out.println("Worked for " + (System.nanoTime() - lastLoopTime) + "ns");
                long sleep = Math.round(Math.floor(((lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000)));
                if (sleep > 0) {
//                    System.out.println("Sleeping for: " + sleep);
                    Thread.sleep(sleep);
                } //else {
//                    System.out.println("Too busy to sleep! Sleep: " + sleep);
//                }
            } catch (InterruptedException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        renderSystem.stop();
    }
}