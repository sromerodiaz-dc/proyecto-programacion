package EDITOR.TEST;

import EDITOR.FX.Main;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TestMain {

    @Test
    public void testMain() {
        // Test the main method
        assertDoesNotThrow(Main::main);
    }
}