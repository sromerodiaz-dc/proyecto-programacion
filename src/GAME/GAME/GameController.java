package GAME.GAME;

import GAME.ENTITY.Player;
import GAME.GPHICS.PiezaManager;

public class GameController {
    private Player model;
    private PiezaManager piezaManager;

    public GameController(Player model, PiezaManager piezaManager) {
        this.model = model;
        this.piezaManager = piezaManager;
    }

    public PiezaManager getPiezaManager() {
        return piezaManager;
    }

    public void update() {
        // Actualiza el estado del jugador
        model.actualiza();
    }
}