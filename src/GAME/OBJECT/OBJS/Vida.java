package GAME.OBJECT.OBJS;

import GAME.ENTITY.Entity;
import GAME.GAME.TeisPanel;

public class Vida extends Entity {
    public Vida(TeisPanel teisPanel) {
        super(teisPanel);

        // Establece el identificador de la puerta
        id = "Vida";

        // Establece que la puerta tiene colisiones activadas
        collision = true;

        image = setEntitySprite("npc/heart_full.png", 48, 48);
        image2 = setEntitySprite("npc/heart_half.png", 48, 48);
        image3 = setEntitySprite("npc/heart_blank.png", 48, 48);
    }
}
