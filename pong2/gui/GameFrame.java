package pong2.gui;

import ecs.EntityManager;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    public GameFrame(EntityManager entityManager) {
        super("Pong");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gamePanel = new GamePanel(entityManager);
        setContentPane(gamePanel);
//        setResizable(false);
//        setLocationRelativeTo(null);
//        pack();
        setSize(900, 600);
        setVisible(true);
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

//        new SwingWorker() {
//            @Override
//            protected String doInBackground() throws InterruptedException {
//                return null;
//            }
//        }.execute();
}
