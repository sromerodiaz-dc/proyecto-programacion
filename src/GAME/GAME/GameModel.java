package GAME.GAME;

import GAME.ENTITY.Player;
import GAME.FX.KeyManager;

public class GameModel {
    private final Player player;

    public GameModel(TeisPanel t, KeyManager k) {
        player = new Player(t, k);
    }

    public Player getPlayer() {
        return player;
    }
}