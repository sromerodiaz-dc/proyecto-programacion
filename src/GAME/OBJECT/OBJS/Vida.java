package GAME.OBJECT.OBJS;

import GAME.ENTITY.Entity;
import GAME.ENTITY.Propierties;
import GAME.GAME.TeisPanel;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Clase que representa una entidad de vida en el juego.
 */
public class Vida extends Entity {
    /**
     * Constructor de la entidad de vida.
     *
     * @param teisPanel panel del juego
     * @param propierties propiedades de la entidad
     */
    public Vida(TeisPanel teisPanel, Propierties propierties) {
        super(teisPanel, propierties);

        // Establece el identificador de la entidad de vida
        id = "Vida";

        // Activa las colisiones para la entidad de vida
        collision = true;

        // Establece las imágenes para la entidad de vida
        image = setEntitySprite("npc/heart_full.png", 48, 48); // Imagen de corazón completo
        image2 = setEntitySprite("npc/heart_half.png", 48, 48); // Imagen de corazón medio
        image3 = setEntitySprite("npc/heart_blank.png", 48, 48); // Imagen de corazón vacío
    }
}
