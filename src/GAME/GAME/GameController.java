package GAME.GAME;

import GAME.GPHICS.PiezaManager;

public class GameController {
    private GameModel model;
    private PiezaManager piezaManager;

    public GameController(GameModel model, PiezaManager piezaManager) {
        this.model = model;
        this.piezaManager = piezaManager;
    }

    public PiezaManager getPiezaManager() {
        return piezaManager;
    }

    public void update() {
        // Actualiza el estado del jugador
        model.getPlayer().actualiza();
    }
}