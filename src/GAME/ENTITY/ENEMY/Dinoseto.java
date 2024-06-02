package GAME.ENTITY.ENEMY;

import GAME.ENTITY.Entity;
import GAME.ENTITY.Propierties;
import GAME.GAME.TeisPanel;

public class Dinoseto extends Entity {
    Propierties propierties;
    public Dinoseto (TeisPanel teisPanel, Propierties propierties) {
        super(teisPanel, propierties);
        this.propierties = propierties;

        setPropierties("Dinoseto_elegante");

        defaultSolidAreaX = solidArea.x;
        defaultSolidAreaY = solidArea.y;

        getImage();
    }

    public void getImage() {
        left1 = setEntitySprite("npc/enemies/dinoseto_left1.png", width, height);
        left2 = setEntitySprite("npc/enemies/dinoseto_left2.png", width, height);
        right1 = setEntitySprite("npc/enemies/dinoseto_right1.png", width, height);
        right2 = setEntitySprite("npc/enemies/dinoseto_right2.png", width, height);
        up1 = setEntitySprite("npc/enemies/dinoseto_right1.png", width, height);
        up2 = setEntitySprite("npc/enemies/dinoseto_right2.png", width, height);
        down1 = setEntitySprite("npc/enemies/dinoseto_right1.png", width, height);
        down2 = setEntitySprite("npc/enemies/dinoseto_right2.png", width, height);
        stop = setEntitySprite("npc/enemies/dinoseto_stop1.png", width, height);
        stop2 = setEntitySprite("npc/enemies/dinoseto_stop2.png", width, height);
    }

    public void setEvent() {
        capEvent++;
        if (capEvent == 120) {
            sentido = moveRandomEntity();
            capEvent = 0;
        }
    }
}
