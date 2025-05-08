package com.game.entity.npc.aggressive;

import com.game.entity.Entity;
import com.game.data.Properties;
import com.game.controller.TeisPanel;

/**
 * Clase que representa al enemigo "Dinoseto" en el juego.
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class Dinoseto extends Entity {
    /**
     * Referencia a las propiedades del juego.
     */
    Properties properties;

    /**
     * Constructor de la clase Dinoseto.
     *
     * @param teisPanel El panel de juego de Teis.
     * @param properties Las propiedades del juego.
     */
    public Dinoseto(TeisPanel teisPanel, Properties properties, int worldX, int worldY) {
        super(teisPanel, properties);
        this.properties = properties;
        this.worldX = worldX;
        this.worldY = worldY;

        // Establece las propiedades específicas del enemigo Dinoseto
        setPropierties("Dinoseto_elegante");

        // Valores de ataque y defensa
        attackVal = 5;
        defenseVal = 10;

        // Guarda la posición del área sólida por defecto
        defaultSolidAreaX = solidArea.x;
        defaultSolidAreaY = solidArea.y;

        // Carga las imágenes del enemigo
        getDinoImage();
    }

    /**
     * Carga las imágenes del enemigo Dinoseto.
     */
    public void getDinoImage() {
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
    public void randomMovement() {
        // Incrementa el contador de eventos
        capEvent++;
        //System.out.println("Posicion del dinoseto:" + worldX +" :" + worldY);
        // Cada 120 frames, cambia la dirección del enemigo de manera aleatoria
        if (capEvent == 120) {
            sentido = moveRandomEntity();
            capEvent = 0;
        }
    }
}
