package pong2.subsystems;

import com.google.common.collect.Sets;
import ecs.EntityManager;
import ecs.SubSystem;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;

public class InputSystem implements SubSystem {
    private final HashMap<Character, Runnable> controlActions = new HashMap<>();

    private HashSet<Character> keysDown = new HashSet<>();

    public InputSystem() {}

    public void start(RenderSystem renderSystem) {
        EventQueue.invokeLater(() -> {
            if (renderSystem.getGamePanel() != null) {
                renderSystem.getGamePanel().addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        super.keyPressed(e);
                        keysDown.add(e.getKeyChar());
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        super.keyReleased(e);
                        keysDown.remove(e.getKeyChar());
                    }
                });
            }
        });
    }

    public boolean addControl(Character character, Runnable runnable) {
        synchronized (controlActions) {
            if (controlActions.containsKey(character)) {
                return false;
            }
            controlActions.put(character, runnable);
            return true;
        }
    }

    public boolean duplicateControl(Character character, Character... characters) {
        HashSet<Character> copyCharacters = Sets.newHashSet(characters);
        if (controlActions.containsKey(character) && Sets.intersection(controlActions.keySet(), copyCharacters).isEmpty()) {
            for (Character c : copyCharacters) {
                controlActions.put(c, controlActions.get(character));
            }
            return true;
        }
        return false;
    }

    public boolean removeControl(Character character) {
        if (controlActions.containsKey(character)) {
            controlActions.remove(character);
            return true;
        }
        return false;
    }

    public boolean removeControl(Character character, Runnable runnable) {
        return controlActions.remove(character, runnable);
    }

    public boolean changeControl(Character characterFrom, Character characterTo) {
        if (controlActions.containsKey(characterFrom) && !controlActions.containsKey(characterTo)) {
            duplicateControl(characterFrom, characterTo);
            removeControl(characterFrom);
            return true;
        }
        return false;
    }

    @Override
    public void processOneGameTick(EntityManager entityManager, double lastFrameTime) {
        keysDown.stream().filter(controlActions::containsKey).forEach(key -> controlActions.get(key).run());
    }
}
