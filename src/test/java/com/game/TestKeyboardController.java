package com.game;

import com.game.controller.KeyboardController;
import com.game.controller.TeisPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.VK_UP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestKeyboardController {

    private KeyboardController keyboardController;

    @BeforeEach
    public void setUp() {
        keyboardController = new KeyboardController(new TeisPanel());
    }

    @Test
    void keyTyped() {
        // KeyboardController's keyTyped method does not change the state, so we cannot test it directly
        // Instead, we can test the key variable after simulating keyPressed and keyReleased events
    }

    @Test
    void keyPressed() {
        boolean initialKeyValue = keyboardController.up;

        KeyEvent KeyEvent = null;
        keyboardController.keyPressed(KeyEvent);

        assertNotEquals(initialKeyValue, keyboardController.up);
        assertEquals(VK_UP, keyboardController.up);
    }

    @Test
    void keyReleased() {
        KeyEvent KeyEvent = null;
        keyboardController.keyPressed(KeyEvent);

        boolean initialKeyValue = keyboardController.down;

        keyboardController.keyReleased(KeyEvent);

        assertEquals(0, keyboardController.down);
        assertNotEquals(initialKeyValue, keyboardController.down);
    }
}