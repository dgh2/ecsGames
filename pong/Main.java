package pong;

import ecs.EntityManager;
import ecs.NonExistentEntityException;
import pong.components.Input;
import pong.components.Position;
import pong.components.Renderable;
import pong.components.Velocity;
import pong.components.shapes.Circle;
import pong.components.shapes.Rectangle;
import pong.components.shapes.Text;
import pong.subsystems.InputSystem;
import pong.subsystems.PhysicsSystem;
import pong.subsystems.RenderSystem;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.util.Random;
import java.util.UUID;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

public class Main extends JFrame {
    private final JPanel jContentPane = new JPanel(new BorderLayout());

    private final EntityManager entityManager = new EntityManager();
    private final RenderSystem renderSystem = new RenderSystem(entityManager);
    private final PhysicsSystem physicsSystem = new PhysicsSystem();
    private final InputSystem inputSystem = new InputSystem(this);

    private UUID fpsLabel;
    private UUID background;
    private UUID paddle1;
    private UUID paddle2;
    private UUID ball;

    private boolean gameRunning = true;
    private Renderable fpsLabelRenderable = new Renderable(1, new Text("FPS: ?"), Color.WHITE);

    public static int WALL_BUFFER_SPACE = 10;
    public static double FPS_WEIGHT_RATIO = 0.9;

    public Main() {
        super();
        initializeGui();
        new SwingWorker() {
            @Override
            protected String doInBackground() throws InterruptedException {
                initializeInput();
                initializeGame();
                return null;
            }
        }.execute();
    }

    private void initializeInput() {
        inputSystem.addControl('q', () -> handleInput(paddle1, 3, 90));
        inputSystem.duplicateControl('q', 'w', 'e', 'r');
        inputSystem.addControl('a', () -> handleInput(paddle1, 3, 270));
        inputSystem.duplicateControl('a', 's', 'd', 'f');
        inputSystem.addControl('u', () -> handleInput(paddle2, 3, 90));
        inputSystem.duplicateControl('u', 'i', 'o', 'p');
        inputSystem.addControl('j', () -> handleInput(paddle2, 3, 270));
        inputSystem.duplicateControl('j', 'k', 'l', ';');
    }

    private void handleInput(UUID entity, double force, double direction) {
        try {
            Input input = entityManager.getComponent(entity, Input.class);
            input.setDirection(direction);
            input.setForce(force);
        } catch (NonExistentEntityException e) {
            e.printStackTrace();
        }
    }

    private void initializeGui() {
        setTitle("Pong");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        jContentPane.add(renderSystem, BorderLayout.CENTER);
        setContentPane(jContentPane);
//        pack();
        setSize(900, 600);
        setVisible(true);
    }

    private void initializeGame() {
        fpsLabel = entityManager.createEntity("fpsLabel");
        background = entityManager.createEntity("background");
        paddle1 = entityManager.createEntity("paddle1");
        paddle2 = entityManager.createEntity("paddle2");
        ball = entityManager.createEntity("ball");
        Random random = new Random();
        try {
            entityManager.addComponent(fpsLabel, new Position(50, 3));
            entityManager.addComponent(fpsLabel, fpsLabelRenderable);
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
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Main::new);
    }

    public void processOneGameTick(double lastFrameTime) {
        physicsSystem.processOneGameTick(entityManager, lastFrameTime);
        renderSystem.processOneGameTick(entityManager, lastFrameTime);
        inputSystem.processOneGameTick(entityManager, lastFrameTime);
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

            ((Text) fpsLabelRenderable.getShape()).setText("FPS: " + Math.round(avgFps));

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
    }
}