package com.game.entity.npc;

import com.game.entity.Entity;
import com.game.data.Properties;
import com.game.controller.TeisPanel;

/**
 * Define al NPC: Viello
 * "Se dice que cuando pierde el Celta,
 * Viello, algo se entristece y, sin embargo,
 * no se sorprende"
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * */
public class Viello extends Entity {
    TeisPanel teisPanel;
    Properties properties;

    // TODO patron Observer para los eventos
    // ! Aplicar el patron para que cuando el jugador lance un evento este llegue a este npc


    /**
     * Constructor de la clase Viello, que representa un anciano en el juego.
     *
     * @param teisPanel panel donde se dibujará el anciano
     */
    public Viello(TeisPanel teisPanel, Properties properties, int worldX, int worldY) {
        // Llama al constructor de la clase padre (suponiendo que es una entidad en el juego)
        super(teisPanel, properties);
        this.teisPanel = teisPanel;
        this.properties = properties;
        this.worldX = worldX;
        this.worldY = worldY;

        // Pasa por parametro el identificador = 1;
        setPropierties("Viello");

        defaultSolidAreaX = solidArea.x;
        defaultSolidAreaY = solidArea.y;

        // Carga las imágenes del anciano
        getVielloImage();
        // Carga los dialogos
        setDialogo();
    }

    public void getVielloImage() {
        // Carga las imágenes del jugador caminando hacia arriba y las establece en las variables correspondientes
        up1 = setEntitySprite("npc/viello/viello_up1.png", width, height);
        up2 = setEntitySprite("npc/viello/viello_up2.png", width, height);

        // Carga las imágenes del jugador caminando hacia abajo y las establece en las variables correspondientes
        down1 = setEntitySprite("npc/viello/viello_down1.png", width, height);
        down2 = setEntitySprite("npc/viello/viello_down2.png", width, height);

        // Carga las imágenes del jugador caminando hacia la izquierda y las establece en las variables correspondientes
        left1 = setEntitySprite("npc/viello/viello_left1.png", width, height);
        left2 = setEntitySprite("npc/viello/viello_left2.png", width, height);

        // Carga las imágenes del jugador caminando hacia la derecha y las establece en las variables correspondientes
        right1 = setEntitySprite("npc/viello/viello_right1.png", width, height);
        right2 = setEntitySprite("npc/viello/viello_right2.png", width, height);

        // Carga las imágenes del jugador detenido y las establece en las variables correspondientes
        stop = setEntitySprite("npc/viello/viello_stop1.png", width, height);
        stop2 = setEntitySprite("npc/viello/viello_stop2.png", width, height);
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

    /**
     * Establece los diálogos del enemigo Dinoseto.
     */
    public void setDialogo() {
        // Establece los diálogos del enemigo
        dialogos[0] = "mozo... \nsabes o que din dos pimentitos de padrón...?";
        dialogos[1] = "\"uns pican... e outros non\" \n*risa jodidamente incontenible*";
        dialogos[2] = "Deus deume o peor dos destinos deste mundo, \nser do Celta.";
    }

    /**
     * Metodo para que el enemigo hable.
     */
    public void fala() {
        // Llama al metodo fala() de la clase padre (Entity) para futuras modificaciones
        super.fala();
    }
}
