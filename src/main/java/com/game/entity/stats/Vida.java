package com.game.entity.stats;

import com.game.data.Properties;
import com.game.entity.Entity;
import com.game.controller.TeisPanel;

/**
 * Clase que representa una entidad de vida en el juego.
 *
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class Vida extends Entity {
    /**
     * Constructor de la entidad de vida.
     *
     * @param teisPanel panel del juego
     * @param properties propiedades de la entidad
     */
    public Vida(TeisPanel teisPanel, Properties properties) {
        super(teisPanel, properties);

        // Establece el identificador de la entidad de vida
        name = "Vida";

        // Activa las colisiones para la entidad de vida
        collision = true;

        // Establece las imágenes para la entidad de vida
        image = setEntitySprite("npc/heart_full.png", 48, 48); // Imagen de corazón completo
        image2 = setEntitySprite("npc/heart_half.png", 48, 48); // Imagen de corazón medio
        image3 = setEntitySprite("npc/heart_blank.png", 48, 48); // Imagen de corazón vacío
    }
}
