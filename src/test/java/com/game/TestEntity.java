package com.game;

import com.game.entity.Entity;
import com.game.entity.Player;
import com.game.entity.Propierties;
import com.game.manager.KeyManager;
import com.game.controller.TeisPanel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.LineUnavailableException;

class TestEntity {

    @Test
    void testMoveLeft() throws LineUnavailableException {
        Entity entity = new Entity(new TeisPanel(), new Propierties("as","fas","fas"));
        Player player = new Player(new TeisPanel(), new KeyManager(new TeisPanel()),new Propierties("das","ads","123"));
        player.actualiza();
        Assertions.assertEquals(entity.worldX, entity.worldX - entity.speed);
        Assertions.assertEquals('a', entity.sentido);
    }
}