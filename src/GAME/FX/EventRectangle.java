package GAME.FX;

import java.awt.*;

public class EventRectangle extends Rectangle {
    int defaultX, defaultY, cooldown;
    boolean done = false;

    public void resetCooldowns(int seg_cooldown) {
        cooldown++;
        if (cooldown > seg_cooldown*30) {
            cooldown = 0;
            done = false;
        }
    }
}
