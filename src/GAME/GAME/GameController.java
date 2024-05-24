package GAME.GAME;

import GAME.ENTITY.Player;
import GAME.GPHICS.PiezaManager;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Mantiene una referencia al jugador y al gestor de piezas del juego.
 * */
public class GameController {
    /**
     * El jugador del juego.
     */
    private Player model;

    /**
     * El gestor de piezas del juego.
     */
    private PiezaManager piezaManager;

    /**
     * Constructor que inicializa el controlador del juego con el jugador y el gestor de piezas.
     *
     * @param model El jugador del juego.
     * @param piezaManager El gestor de piezas del juego.
     */
    public GameController(Player model, PiezaManager piezaManager) {
        this.model = model;
        this.piezaManager = piezaManager;
    }

    /**
     * Obtiene el gestor de piezas del juego.
     *
     * @return El gestor de piezas del juego.
     */
    public PiezaManager getPiezaManager() {
        return piezaManager;
    }

    /**
     * Actualiza el estado del juego.
     * Actualiza el estado del jugador y de las piezas del juego.
     */
    public void update() {
        // Actualiza el estado del jugador
        model.actualiza();
    }
}