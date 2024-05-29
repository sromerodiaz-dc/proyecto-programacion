package GAME.OBJECT.OBJS;

import GAME.ENTITY.Entity;
import GAME.GAME.TeisPanel;

public class Container extends Entity {
    /**
     * Constructor que inicializa el Passvigo con sus propiedades específicas.
     */
    public Container(TeisPanel teisPanel) {
        super(teisPanel);

        // Establece el identificador del Passvigo
        id = "Container";

        // Establece que la puerta tiene colisiones activadas
        collision = true;

        stop = setEntitySprite("objects/contenedor.png");
    }
}
