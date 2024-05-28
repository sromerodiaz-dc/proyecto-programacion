package GAME.ENTITY.NPC;

import GAME.ENTITY.Entity;
import GAME.GAME.TeisPanel;

public class Viello extends Entity {
    public Viello (TeisPanel teisPanel) {
        super(teisPanel);

        sentido = '0';
        speed = 2; // Se supone que es un anciando así que se mueve lento
    }
}
