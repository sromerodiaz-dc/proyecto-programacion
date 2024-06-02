package GAME.ENTITY.NPC;

import GAME.ENTITY.Entity;
import GAME.ENTITY.Propierties;
import GAME.GAME.TeisPanel;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Define al NPC: Viello
 * "Se dice que cuando pierde el Celta,
 * Viello, algo se entristece y, sin embargo,
 * no se sorprende"
 * */
public class Viello extends Entity {
    TeisPanel teisPanel;
    Propierties propierties;
    /**
     * Constructor de la clase Viello, que representa un anciano en el juego.
     *
     * @param teisPanel panel donde se dibujará el anciano
     */
    public Viello(TeisPanel teisPanel, Propierties propierties) {
        // Llama al constructor de la clase padre (suponiendo que es una entidad en el juego)
        super(teisPanel,propierties);
        this.teisPanel = teisPanel;
        this.propierties = propierties;

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

    public void setEvent() {
        capEvent++;
        if (capEvent == 120) {
            sentido = moveRandomEntity();
            capEvent = 0;
        }
    }

    public void setDialogo() {
        dialogos[0] = "mozo... \nsabes o que din dos pimentitos de padrón...?";
        dialogos[1] = "\"uns pican... e outros non\" \n*risa jodidamente incontenible*";
        dialogos[2] = "Deus deume o peor dos destinos deste mundo, \nser do Celta.";
    }

    public void fala() {

        // Necesario para futuras modificaciones
        super.fala();
    }
}
