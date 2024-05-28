package GAME.TEST;

import GAME.ENTITY.Entity;
import GAME.GAME.TeisPanel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestPlayer {

    @Test
    void testSetValoresPorDefecto() {
        Entity entity = new Entity(new TeisPanel());
        TeisPanel teisPanel = null;
        assertEquals(entity.worldX, 15 * teisPanel.sizeFinal);
        assertEquals(entity.worldY, 15 * teisPanel.sizeFinal);
        assertEquals(entity.speed, 5);
        assertEquals(entity.sentido, '0');
    }

    @Test
    void testGetPlayerImage() {
        Entity entity = new Entity(new TeisPanel());

        try {
            assertNotNull(entity.up1);
            assertNotNull(entity.up2);
            assertNotNull(entity.down1);
            assertNotNull(entity.down2);
            assertNotNull(entity.left1);
            assertNotNull(entity.left2);
            assertNotNull(entity.right1);
            assertNotNull(entity.right2);
            assertNotNull(entity.stop);
            assertNotNull(entity.stop2);

        } catch (RuntimeException e) {
            fail("getPlayerImage() failed: " + e.getMessage());
        }
    }

    @Test
    void testActualiza() {
        Entity entity = new Entity(new TeisPanel());
    }
}