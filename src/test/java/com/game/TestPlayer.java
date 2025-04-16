package com.game;

import com.game.entity.Entity;
import com.game.entity.Propierties;
import com.game.controller.TeisPanel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestPlayer {
    @Test
    void testGetPlayerImage() {
        Entity entity = new Entity(new TeisPanel(),new Propierties("asf","as","123"));

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
        Entity entity = new Entity(new TeisPanel(),new Propierties("asf","as","123"));
    }
}