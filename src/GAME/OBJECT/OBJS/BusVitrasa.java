package GAME.OBJECT.OBJS;

import GAME.ENTITY.Entity;
import GAME.GAME.TeisPanel;

public class BusVitrasa extends Entity {

    public BusVitrasa(TeisPanel teisPanel) {
        super(teisPanel);

        // Establece el identificador del Passvigo
        id = "Bus";

        // Establece que la puerta tiene colisiones activadas
        collision = true;
        stop = setEntitySprite("objects/busVitrasa.png");
    }
}
