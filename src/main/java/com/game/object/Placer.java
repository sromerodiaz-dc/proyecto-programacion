package com.game.object;

import com.game.entity.enemy.Dinoseto;
import com.game.entity.npc.Viello;
import com.game.data.Propierties;
import com.game.controller.TeisPanel;

/**
 * Clase que se encarga de colocar objetos, NPC y enemigos en el panel de juego.
 *
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class Placer {
    /**
     * Referencia al panel de juego donde se colocarán los objetos.
     */
    private final TeisPanel teisPanel;

    /**
     * Propiedades del juego.
     */
    private final Propierties propierties;

    /**
     * Constructor que inicializa la referencia al panel de juego y las propiedades del juego.
     *
     * @param teisPanel el panel de juego donde se colocarán los objetos
     * @param propierties propiedades del juego
     */
    public Placer(TeisPanel teisPanel, Propierties propierties) {
        this.teisPanel = teisPanel;
        this.propierties = propierties;
    }

    /**
     * Coloca todos los recursos en el panel de juego, incluyendo NPC y enemigos.
     */
    public void setRecursos() {
        setNPC(); // Coloca los NPC en el panel de juego
        setEnemy(); // Coloca los enemigos en el panel de juego
    }

    /**
     * Coloca un NPC específico en el panel de juego.
     */
    public void setNPC() {
        // Crea un nuevo NPC de tipo Viello y lo agrega al array de NPC del controlador
        teisPanel.controller.npc.add(new Viello(teisPanel, propierties));

        // Establece la posición del NPC en el mundo
        teisPanel.controller.npc.getFirst().worldX = teisPanel.sizeFinal * 19;
        teisPanel.controller.npc.getFirst().worldY = teisPanel.sizeFinal * 12;
    }

    /**
     * Coloca un enemigo específico en el panel de juego.
     */
    public void setEnemy() {
        // Crea un nuevo enemigo de tipo Dinoseto y lo agrega al array de enemigos del controlador
        teisPanel.controller.enemy.add(new Dinoseto(teisPanel, propierties));

        // Establece la posición del enemigo en el mundo
        teisPanel.controller.enemy.getFirst().worldX = teisPanel.sizeFinal * 5;
        teisPanel.controller.enemy.getFirst().worldY = teisPanel.sizeFinal * 12;
    }
}
