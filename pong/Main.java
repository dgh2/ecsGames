package game1;

import ecs.EntityManager;
import ecs.NonExistentEntityException;
import game1.components.Collidable;
import game1.components.Input;
import game1.components.Position;
import game1.components.Renderable;
import game1.components.Velocity;
import game1.components.shapes.Circle;
import game1.components.shapes.Rectangle;
import game1.components.shapes.Text;
import game1.subsystems.InputSystem;
import game1.subsystems.PhysicsSystem;
import game1.subsystems.RenderSystem;
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
    private final InputSystem inputSystem = new InputSystem(jContentPane);

    private UUID fpsLabel;
    private UUID background;
    private UUID paddle1;
    private UUID paddle2;
    private UUID ball;

    private boolean gameRunning = true;
    private Renderable fpsLabelRenderable = new Renderable(1, new Text("FPS: ?"), Color.WHITE);

    public static int WALL_BUFFER_SPACE = 0;
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
        inputSystem.addControl('q', () -> handleInput(paddle1, 3, 0));
        inputSystem.duplicateControl('q', 'w', 'e', 'r');
        inputSystem.addControl('a', () -> handleInput(paddle1, 3, 180));
        inputSystem.duplicateControl('a', 's', 'd', 'f');
        inputSystem.addControl('u', () -> handleInput(paddle2, 3, 0));
        inputSystem.duplicateControl('u', 'i', 'o', 'p');
        inputSystem.addControl('j', () -> handleInput(paddle2, 3, 180));
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
            entityManager.addComponent(background, new Renderable(0, new Rectangle(getWidth(), getHeight()), Color.BLACK));
            entityManager.addComponent(background, new Collidable());
            entityManager.addComponent(paddle1, new Velocity(0, 0, 0.66));
            Renderable paddle1Renderable = new Renderable(1, new Rectangle(10, getHeight() * .1), Color.WHITE);
            entityManager.addComponent(paddle1, paddle1Renderable);
            entityManager.addComponent(paddle1, new Collidable());
            entityManager.addComponent(paddle1,
                    new Position(WALL_BUFFER_SPACE,
                            Math.round(getHeight() - ((Rectangle) paddle1Renderable.getShape()).getHeight()) / 2));
            entityManager.addComponent(paddle1, new Input(.5));
            entityManager.addComponent(paddle2, new Velocity(0, 0, 0.66));
            Renderable paddle2Renderable = new Renderable(1, new Rectangle(10, getHeight() * .1), Color.WHITE);
            entityManager.addComponent(paddle2, paddle2Renderable);
            entityManager.addComponent(paddle2,
                    new Position(Math.round(getWidth() - ((Rectangle) paddle2Renderable.getShape()).getWidth() - WALL_BUFFER_SPACE),
                            Math.round(getHeight() - ((Rectangle) paddle2Renderable.getShape()).getHeight()) / 2));
            Renderable ballRenderable = new Renderable(2, new Circle(8), Color.WHITE);
            entityManager.addComponent(paddle2, new Input(.5));
            entityManager.addComponent(paddle2, new Collidable());
            entityManager.addComponent(ball, ballRenderable);
            entityManager.addComponent(ball,
                    new Position(Math.round(Math.round((getWidth() - ((Circle) ballRenderable.getShape()).getDiameter()) / 2)),
                            Math.round(Math.round(((getHeight() - ((Circle) ballRenderable.getShape()).getDiameter()) / 2)))));
            entityManager.addComponent(ball, new Velocity(random.nextInt(5) + 3,
                    (random.nextInt(/*9*/0) + 45) * (random.nextBoolean() ? 1 : -1), 0.001));
            entityManager.addComponent(ball, new Collidable());
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