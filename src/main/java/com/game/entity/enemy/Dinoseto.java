package com.game.entity.enemy;

import com.game.entity.Entity;
import com.game.entity.Propierties;
import com.game.controller.TeisPanel;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Clase que representa al enemigo "Dinoseto" en el juego.
 */
public class Dinoseto extends Entity {
    /**
     * Referencia a las propiedades del juego.
     */
    Propierties propierties;

    /**
     * Constructor de la clase Dinoseto.
     *
     * @param teisPanel El panel de juego de Teis.
     * @param propierties Las propiedades del juego.
     */
    public Dinoseto(TeisPanel teisPanel, Propierties propierties) {
        super(teisPanel, propierties);
        this.propierties = propierties;

        // Establece las propiedades específicas del enemigo Dinoseto
        setPropierties("Dinoseto_elegante");

        // Guarda la posición del área sólida por defecto
        defaultSolidAreaX = solidArea.x;
        defaultSolidAreaY = solidArea.y;

        // Carga las imágenes del enemigo
        getImage();
    }

    /**
     * Carga las imágenes del enemigo Dinoseto.
     */
    public void getImage() {
        // Carga las imágenes para cada dirección y estado del enemigo
        left1 = setEntitySprite("npc/enemies/dinoseto_left1.png", width, height);
        left2 = setEntitySprite("npc/enemies/dinoseto_left2.png", width, height);
        right1 = setEntitySprite("npc/enemies/dinoseto_right1.png", width, height);
        right2 = setEntitySprite("npc/enemies/dinoseto_right2.png", width, height);
        up1 = setEntitySprite("npc/enemies/dinoseto_right1.png", width, height);
        up2 = setEntitySprite("npc/enemies/dinoseto_right2.png", width, height);
        down1 = setEntitySprite("npc/enemies/dinoseto_right1.png", width, height);
        down2 = setEntitySprite("npc/enemies/dinoseto_right2.png", width, height);
        stop = setEntitySprite("npc/enemies/dinoseto_stop1.png", width, height);
        stop2 = setEntitySprite("npc/enemies/dinoseto_stop2.png", width, height);
    }

    /**
     * Establece el evento de movimiento aleatorio del enemigo.
     */
    public void setEvent() {
        // Incrementa el contador de eventos
        capEvent++;

        // Cada 120 frames, cambia la dirección del enemigo de manera aleatoria
        if (capEvent == 120) {
            sentido = moveRandomEntity();
            capEvent = 0;
        }
    }
}
