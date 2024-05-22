package GAME.TEST;

import GAME.FX.KeyManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.VK_UP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestKeyManager {

    private KeyManager keyManager;

    @BeforeEach
    public void setUp() {
        keyManager = new KeyManager();
    }

    @Test
    void keyTyped() {
        // KeyManager's keyTyped method does not change the state, so we cannot test it directly
        // Instead, we can test the key variable after simulating keyPressed and keyReleased events
    }

    @Test
    void keyPressed() {
        boolean initialKeyValue = keyManager.up;

        KeyEvent KeyEvent = null;
        keyManager.keyPressed(KeyEvent);

        assertNotEquals(initialKeyValue, keyManager.up);
        assertEquals(VK_UP, keyManager.up);
    }

    @Test
    void keyReleased() {
        KeyEvent KeyEvent = null;
        keyManager.keyPressed(KeyEvent);

        boolean initialKeyValue = keyManager.down;

        keyManager.keyReleased(KeyEvent);

        assertEquals(0, keyManager.down);
        assertNotEquals(initialKeyValue, keyManager.down);
    }
}