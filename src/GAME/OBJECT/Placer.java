package GAME.OBJECT;

import GAME.ENTITY.ENEMY.Dinoseto;
import GAME.ENTITY.NPC.Viello;
import GAME.ENTITY.Propierties;
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

    Propierties propierties;

    /**
     * Constructor que inicializa la referencia al panel de juego.
     *
     * @param teisPanel el panel de juego donde se colocarán los objetos
     */
    public Placer(TeisPanel teisPanel, Propierties propierties) {
        this.teisPanel = teisPanel; this.propierties = propierties;
    }

    /**
     * Coloca objetos / NPC / etc. en el panel de juego.
     */
    public void setRecursos() {
        setNPC();
        setEnemy();
    }

    public void setNPC() {
        teisPanel.controller.npc[0] = new Viello(teisPanel, propierties);
        teisPanel.controller.npc[0].worldX = teisPanel.sizeFinal * 19;
        teisPanel.controller.npc[0].worldY = teisPanel.sizeFinal * 12;
    }

    public void setEnemy() {
        teisPanel.controller.enemy[0] = new Dinoseto(teisPanel, propierties);
        teisPanel.controller.enemy[0].worldX = teisPanel.sizeFinal * 5;
        teisPanel.controller.enemy[0].worldY = teisPanel.sizeFinal * 12;
    }
}
