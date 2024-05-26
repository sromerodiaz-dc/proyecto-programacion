package GAME.TEST;

import GAME.ENTITY.Entity;
import GAME.ENTITY.Player;
import GAME.ENTITY.Propierties;
import GAME.FX.KeyManager;
import GAME.GAME.TeisPanel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.LineUnavailableException;
import java.awt.*;

class TestEntity {

    @Test
    void testMoveLeft() throws LineUnavailableException {
        Entity entity = new Entity(new TeisPanel(), new Propierties("as","fas","fas"));
        Player player = new Player(new TeisPanel(), new KeyManager(new TeisPanel()),new Propierties("das","ads","123"));
        player.actualiza();
        Assertions.assertEquals(entity.worldX, entity.worldX - entity.speed);
        Assertions.assertEquals(entity.sentido, 'a');
    }
}