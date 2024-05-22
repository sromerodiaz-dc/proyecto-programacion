package GAME.TEST;

import GAME.ENTITY.Entity;
import GAME.FX.KeyManager;
import GAME.GAME.TeisPanel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

class EntityTest {

    @Test
    void testMoveLeft() {
        Entity entity = new Entity();
        KeyManager keyManager = new KeyManager();
        keyManager.left = true;
        entity.move(keyManager);
        Assertions.assertEquals(entity.worldX, entity.worldX - entity.speed);
        Assertions.assertEquals(entity.sentido, 'a');
    }

    @Test
    void pinta() {
        Entity entity = new Entity();
        TeisPanel teis = new TeisPanel();
        Graphics2D g2 = null;
        entity.pinta(g2, teis);
    }
}