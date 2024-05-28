package GAME.ENTITY.NPC;

import GAME.ENTITY.Entity;
import GAME.GAME.TeisPanel;

public class Viello extends Entity {
    public Viello (TeisPanel teisPanel) {
        super(teisPanel);

        sentido = '0';
        speed = 2; // Se supone que es un anciando así que se mueve lento

        getVielloImage();
        /*solidArea = new Rectangle();
        solidArea.width = 32;
        solidArea.height = 32;*/
    }

    public void getVielloImage() {
        // Carga las imágenes del jugador caminando hacia arriba y las establece en las variables correspondientes
        up1 = setEntitySprite("npc/viello_up1.png");
        up2 = setEntitySprite("npc/viello_up2.png");

        // Carga las imágenes del jugador caminando hacia abajo y las establece en las variables correspondientes
        down1 = setEntitySprite("npc/viello_down1.png");
        down2 = setEntitySprite("npc/viello_down2.png");

        // Carga las imágenes del jugador caminando hacia la izquierda y las establece en las variables correspondientes
        left1 = setEntitySprite("npc/viello_down1.png");
        left2 = setEntitySprite("npc/viello_down2.png");

        // Carga las imágenes del jugador caminando hacia la derecha y las establece en las variables correspondientes
        right1 = setEntitySprite("npc/viello_right1.png");
        right2 = setEntitySprite("npc/viello_right2.png");

        // Carga las imágenes del jugador detenido y las establece en las variables correspondientes
        stop = setEntitySprite("npc/viello_stop1.png");
        stop2 = setEntitySprite("npc/viello_stop2.png");
    }
}
