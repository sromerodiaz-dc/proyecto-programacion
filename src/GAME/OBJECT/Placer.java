package GAME.OBJECT;

import GAME.ENTITY.NPC.Viello;
import GAME.GAME.TeisPanel;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Clase que se encarga de colocar objetos en el panel de juego.
 */
public class Placer {
    /**
     * Referencia al panel de juego donde se colocarán los objetos.
     */
    TeisPanel teisPanel;

    /**
     * Constructor que inicializa la referencia al panel de juego.
     *
     * @param teisPanel el panel de juego donde se colocarán los objetos
     */
    public Placer(TeisPanel teisPanel) {
        this.teisPanel = teisPanel;
    }

    /**
     * Coloca objetos / NPC / etc. en el panel de juego.
     */
    public void setRecursos() {
        teisPanel.controller.npc[0] = new Viello(teisPanel);
        teisPanel.controller.npc[0].worldX = teisPanel.sizeFinal * 18;
        teisPanel.controller.npc[0].worldY = teisPanel.sizeFinal * 11;
    }
}
