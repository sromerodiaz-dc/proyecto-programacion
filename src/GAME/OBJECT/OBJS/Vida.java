package GAME.OBJECT.OBJS;

import GAME.ENTITY.Entity;
import GAME.GAME.TeisPanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Vida extends Entity {
    public Vida(TeisPanel teisPanel) {
        super(teisPanel);

        // Establece el identificador de la puerta
        id = "Vida";

        // Establece que la puerta tiene colisiones activadas
        collision = true;

        image = setEntitySprite("npc/heart_full.png");
        image2 = setEntitySprite("npc/heart_half.png");
        image3 = setEntitySprite("npc/heart_blank.png");
    }
}
