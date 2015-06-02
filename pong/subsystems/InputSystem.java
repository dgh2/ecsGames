package pong.subsystems;

import com.google.common.collect.Sets;
import ecs.EntityManager;
import ecs.SubSystem;
import pong.components.Input;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import javax.swing.JPanel;

public class InputSystem implements SubSystem {
    private HashMap<Character, Runnable> keyMap = new HashMap<>();

    public InputSystem(JPanel jpanel) {
        jpanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (keyMap.containsKey(e.getKeyChar())) {
                    keyMap.get(e.getKeyChar()).run();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
            }
        });
    }

    public boolean addControl(Character character, Runnable runnable) {
        if (keyMap.containsKey(character)) {
            return false;
        }
        keyMap.put(character, runnable);
        return true;
    }

    public boolean duplicateControl(Character character, Character... characters) {
        Runnable copyRunnable = keyMap.get(character);
        HashSet<Character> copyCharacters = Sets.newHashSet(characters);
        if (copyRunnable == null || Sets.intersection(keyMap.keySet(), copyCharacters).isEmpty()) {
            return false;
        }
        for (Character ch : copyCharacters) {
            keyMap.put(ch, copyRunnable);
        }
        return true;
    }

    public boolean removeControl(Character character, Runnable runnable) {
        return keyMap.remove(character, runnable);
    }

    @Override
    public void processOneGameTick(EntityManager entityManager, double lastFrameTime) {
        HashMap<UUID, Input> entityInputMap = entityManager.getEntityComponentMapByClass(Input.class);
        for (Input input : new HashSet<>(entityInputMap.values())) {
            input.setForce(input.getForce() * (1 - input.getDecayRate()));
        }
    }
}
